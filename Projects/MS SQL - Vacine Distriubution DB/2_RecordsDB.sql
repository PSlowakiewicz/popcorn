USE [MagazynSczepionek]
GO
INSERT [dbo].[stanyRealizacji] ([znaczenie]) VALUES 
(N'realizowane'),
(N'Realizacja w przysz³oœci'),
(N'Nieudana realizacja')
GO

INSERT [dbo].[Fabryki] (adres) VALUES 
('Hamburg, Germany'),
('Oxford, England'),
('Catalent, Italy'),
('Szczecin, Poland'),
('Vienna, Austria')
GO

INSERT [dbo].[Firmy] (nazwaFirmy, dniWaznosc, minTeperatura, maxTemperatura, minDni2Dawka, maxDni2Dawka) VALUES 
('Moderna', 30, 2, 8, 28, 90),
('Astra Zeneca', 150, -10, -5, 20, 70),
('Pfizer', 5, -80, -70, 42, 60),
('Sputnik v', 90, 2, 8, 21, 90),
('Johnson & Johnson', 1200, 2, 8, null, null)
GO


INSERT [dbo].[Centrum] (adres, miasto, województwo) VALUES 
(N'Waryñskiego 12', 'Warszawa', 'Mazowieckie'),
(N'Mi³a 1', 'Kraków', 'Ma³opolskie'),
(N'Pi³a 17', 'Warszawa', 'Mazowieckie'),
(N'Warszawska 54', 'Kielce', 'Œwiêtokrzyskie'),
(N'Morska 81', 'Gdañsk', 'Pomorskie')
GO

INSERT [dbo].[Wysylki] (nrCentrum, dataWysylki) VALUES 
(1, '2021-05-15'),
(2, '2021-03-12'),
(3, '2021-05-08'),
(4, '2021-04-20'),
(5, '2021-01-13')
GO

INSERT [dbo].[Szczepionki] (liczbaDawek, dataProdukcji, nrFabryki, nrFirmy, nrWysylki) VALUES 
(100, '2021-03-08',1,2,1),
(30, '2021-05-04',2,4,2),
(10,'2021-04-15',3,1,3),
(80,'2021-05-13',1,3,4),
(22,'2021-01-07',5,5,5),
(100, '2021-03-20',1,2,4),
(30, '2021-05-14',2,4,2),
(10,'2021-05-20',3,1,3),
(80,'2021-05-17',1,3,1),
(40,'2021-02-07',5,5,4)
GO

INSERT [dbo].[Pacienci] (nrCentrum, imie, nazwisko, pesel, dataUrodzenia, miastoUrodzenia, przeciwskazania, opoznienie, kategoria) VALUES
(1, 'Monika', 'Panika', '84032129294', '1984-03-21', 'Warszawa', null, null, 2),
(2, 'Katarzyna', N'Pra¿yna', '75061192276' ,'1975-06-11', 'Kraków', null, null, 0),
(4, 'Marek', 'Karek', '92102472915', '1992-10-24', 'Kielce', null, '2021-05-16', 0),
(5, N'Pawe³', N'Gawe³', '90060566952', '1990-06-05', 'Gdañsk', null, null, 3),
(5, N'Roman', N'Broman', '65111331966', '1965-11-13', 'Gdañsk', null, null, 1),
(3, 'Marzena', 'Bezrymu', '77092154449', '1977-09-21', 'Warszawa', 'Ci¹¿a', '2021-08-15', 1),
(5, 'Test1', 'Test1', '84032129295', '1984-03-21', 'Warszawa', null, null, 2);
GO

INSERT [dbo].[Zlecenia] (nrPacienta, nrPartii, dataZlecenia, realizacja, czyDrugie) VALUES 
(1, 1, '2021-04-15', 1, 0),
(1, 1, '2021-05-16', 1, 1),
(2, 2, '2021-04-21', 1, 0),
(2, 2, '2021-05-16', 1, 1),
(3, 3, '2021-04-15', 1, 0),
(3, 3, '2021-05-15', 3, 1),
(3, 3, '2021-05-17', 1, 1),
(4, 4, '2021-06-05', 2, 0),
(4, 4, '2021-07-15', 3, 1),
(5, 5, '2021-04-23', 1, 0),
(6, 1, '2021-05-16', 3, 0),
(6, 1, '2021-07-16', 2, 1)
GO

update Zlecenia 
set nrPartii = null, dataZlecenia = null, realizacja = 2
where nrPacienta = 6;
GO

update Zlecenia
set dataZlecenia = '2021-06-01', realizacja = 2
where nrPacienta = 5 and czyDrugie = 0;
GO