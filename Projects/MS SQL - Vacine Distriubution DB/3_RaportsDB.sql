USE [MagazynSczepionek]
GO

-- 1 --
Select COUNT(*) as N'Liczba Zleceñ' from Zlecenia
where dataZlecenia <= DATEADD(DD, 7, GETDATE()) 

-- 2 --
Select top 1 nrCentrum, convert(float,100*sum(k.lz))/convert(float,sum(k.dawki)) as 'Zmarnowane' from
(
Select z.nrPartii, nrCentrum,MAX(liczbaDawek) as dawki,MAX(liczbaDawek) - count(nrZlecenia) as 'lz' from Zlecenia z
join Szczepionki s on s.nrPartii = z.nrPartii
join Pacienci p on p.nrPacienta = z.nrPacienta
join Firmy f on f.nrFirmy = s.nrFirmy
where Getdate() > dateadd(DAY, dniWaznosc,dataProdukcji) and realizacja = 1
group by z.nrPartii, p.nrCentrum
) k
group by k.nrCentrum
order by Zmarnowane desc

-- 3 --
Select MAX(nazwaFirmy) as 'Nazwa Firmy', sum(liczbaDawek) as 'Liczba Dawek' from Szczepionki s
join Firmy f on f.nrFirmy = s.nrFirmy
group by s.nrFirmy

-- 4 --
Select MAX(imie) as 'imie', MAX(nazwisko) as 'nazwisko', MAX(pesel) as 'pesel', MAX(p.kategoria) as k from Zlecenia z
join Pacienci p on p.nrPacienta = z.nrPacienta
where (czyDrugie = 1 and opoznienie >= dataZlecenia)
group by p.nrPacienta
order by k

-- 5 --
Select fb.nrFabryki, MAX(fb.adres) as 'Adres', count(nrZlecenia) as 'Wykorzystane' from Zlecenia z
join Szczepionki s on s.nrPartii = z.nrPartii
join Fabryki fb on fb.nrFabryki = s.nrFabryki
where realizacja = 1
group by fb.nrFabryki
order by Wykorzystane desc
