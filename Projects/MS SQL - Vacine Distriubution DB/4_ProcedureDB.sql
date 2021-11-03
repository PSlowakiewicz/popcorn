use MagazynSczepionek
GO
create procedure dodaj
@id_pacienta int = null,
@typ_szczepionki int = null,
@id_centrum int = null,
@data date = null
as
-- sprawdzenie czy pacient nie ma opóŸnienia
if ((Select opoznienie from Pacienci where nrPacienta = @id_pacienta) > @data)
begin
 select 'Wystêpuje opóznienie'
 return 1
end
-- Sprawdzenie czy pacient nie przyj¹³ ju¿ dwóch dawek
if ((Select czyDrugie from Zlecenia z where z.nrPacienta = @id_pacienta and z.realizacja = 1 and czyDrugie = 1) = 1)
begin
 select 'Zosta³y wykonane 2 szczepionki'
 return 1
end
-- Sprawdzenie czy min¹³ odpowiedni czas pomiêdzy szczepionkami
if (dateadd(DAY, (Select f.minDni2Dawka from Szczepionki s join Firmy f on f.nrFirmy = s.nrFirmy where s.nrPartii = @typ_szczepionki),
	(Select MAX(dataZlecenia) from Zlecenia z where z.nrPacienta = 1 and z.realizacja = 1 and czyDrugie = 0 group by nrPacienta)) > @data)
begin
	select 'Zbyt ma³y odstêp czasu'
	return 1
end

declare @id_partii int = null
-- sprawdzamy czy jest mo¿liwoœæ zlecenia szczepionki która jest ju¿ w centrum
set @id_partii = (select top 1 s.nrPartii from Szczepionki s
join Firmy f on f.nrFirmy = s.nrFirmy
join Wysylki w on w.nrWysylki = s.nrWysylki
join Centrum c on c.nrCentrum = w.nrCentrum
join (select count(nrZlecenia) as 'NR', nrPartii from Zlecenia group by nrPartii) k on k.nrPartii = s.nrPartii
where f.nrFirmy = @typ_szczepionki and dateadd(day, dniWaznosc, s.dataProdukcji) >= @data
and c.nrCentrum = @id_centrum and s.liczbaDawek - k.NR > 0);

-- jeœli nie ma to sprawdzamy czy jest mo¿liwoœæ zamówienia takiej szczepionki
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
		select 'Zlecenie wykonane, szczepionka zamówiona z magazynu'
		commit transaction

	end
end
else
begin
	INSERT [dbo].[Zlecenia] (nrPacienta, nrPartii, dataZlecenia, realizacja, czyDrugie) VALUES (@id_pacienta, @id_partii, @data, 2, 0)
	select 'Zlecenie wykonane'
end
go

