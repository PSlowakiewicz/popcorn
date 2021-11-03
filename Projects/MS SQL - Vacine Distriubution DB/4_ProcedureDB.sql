use MagazynSczepionek
GO
create procedure dodaj
@id_pacienta int = null,
@typ_szczepionki int = null,
@id_centrum int = null,
@data date = null
as
-- sprawdzenie czy pacient nie ma op�nienia
if ((Select opoznienie from Pacienci where nrPacienta = @id_pacienta) > @data)
begin
 select 'Wyst�puje op�znienie'
 return 1
end
-- Sprawdzenie czy pacient nie przyj�� ju� dw�ch dawek
if ((Select czyDrugie from Zlecenia z where z.nrPacienta = @id_pacienta and z.realizacja = 1 and czyDrugie = 1) = 1)
begin
 select 'Zosta�y wykonane 2 szczepionki'
 return 1
end
-- Sprawdzenie czy min�� odpowiedni czas pomi�dzy szczepionkami
if (dateadd(DAY, (Select f.minDni2Dawka from Szczepionki s join Firmy f on f.nrFirmy = s.nrFirmy where s.nrPartii = @typ_szczepionki),
	(Select MAX(dataZlecenia) from Zlecenia z where z.nrPacienta = 1 and z.realizacja = 1 and czyDrugie = 0 group by nrPacienta)) > @data)
begin
	select 'Zbyt ma�y odst�p czasu'
	return 1
end

declare @id_partii int = null
-- sprawdzamy czy jest mo�liwo�� zlecenia szczepionki kt�ra jest ju� w centrum
set @id_partii = (select top 1 s.nrPartii from Szczepionki s
join Firmy f on f.nrFirmy = s.nrFirmy
join Wysylki w on w.nrWysylki = s.nrWysylki
join Centrum c on c.nrCentrum = w.nrCentrum
join (select count(nrZlecenia) as 'NR', nrPartii from Zlecenia group by nrPartii) k on k.nrPartii = s.nrPartii
where f.nrFirmy = @typ_szczepionki and dateadd(day, dniWaznosc, s.dataProdukcji) >= @data
and c.nrCentrum = @id_centrum and s.liczbaDawek - k.NR > 0);

-- je�li nie ma to sprawdzamy czy jest mo�liwo�� zam�wienia takiej szczepionki
if (@id_partii is null) 
begin
	set @id_partii = (Select top 1 s.nrPartii from Szczepionki s
		where s.nrWysylki is null)
	if (@id_partii is null)
	begin
		select 'Nie ma takiej szczepionki'
	end
	else
	begin
		begin transaction
		INSERT [dbo].[Wysylki] (nrCentrum, dataWysylki) VALUES (@id_centrum, GETDATE());
		update Szczepionki set nrWysylki = (Select top 1 nrWysylki from Wysylki order by nrWysylki desc) where nrPartii = @id_partii;
		INSERT [dbo].[Zlecenia] (nrPacienta, nrPartii, dataZlecenia, realizacja, czyDrugie) VALUES  (@id_pacienta, @id_partii, @data, 2, 0)
		select 'Zlecenie wykonane, szczepionka zam�wiona z magazynu'
		commit transaction

	end
end
else
begin
	INSERT [dbo].[Zlecenia] (nrPacienta, nrPartii, dataZlecenia, realizacja, czyDrugie) VALUES (@id_pacienta, @id_partii, @data, 2, 0)
	select 'Zlecenie wykonane'
end
go

