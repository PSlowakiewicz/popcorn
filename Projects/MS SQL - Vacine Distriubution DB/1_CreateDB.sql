USE [master]
GO
CREATE DATABASE [MagazynSczepionek]
GO
USE [MagazynSczepionek]
GO
CREATE TABLE [dbo].[Centrum](
	[nrCentrum] [int] NOT NULL PRIMARY KEY Identity(1,1),
	[adres] [varchar](50) NULL,
	[miasto] [varchar](50) NULL,
	[województwo] [varchar](50) NULL,
)
GO
CREATE TABLE [dbo].[Fabryki](
	[nrFabryki] [int] NOT NULL PRIMARY KEY Identity(1,1),
	[adres] [varchar](max) NULL,
	)
GO

CREATE TABLE [dbo].[Firmy](
	[nrFirmy] [int] NOT NULL PRIMARY KEY Identity(1,1),
	[nazwaFirmy] [varchar](30) NOT NULL,
	[dniWaznosc] [int] NOT NULL,
	[minTeperatura] [int] NULL,
	[maxTemperatura] [int] NULL,
	[minDni2Dawka] [int] NULL,
	[maxDni2Dawka] [int] NULL
	)
GO
CREATE TABLE [dbo].[Pacienci](
	[nrPacienta] [int] NOT NULL PRIMARY KEY Identity(1,1),
	[nrCentrum] [int] NOT NULL,
	[imie] [varchar](50) NOT NULL,
	[nazwisko] [varchar](50) NOT NULL,
	[pesel] [varchar](11) NULL,
	[dataUrodzenia] [date] NOT NULL,
	[miastoUrodzenia] [varchar](50) NOT NULL,
	[przeciwskazania] [varchar](max) NULL,
	[opoznienie] [date] NULL,
	[kategoria] [int] NOT NULL check (kategoria >= 0 and kategoria < 5),

	CONSTRAINT [FK_Pacienci_Centrum] FOREIGN KEY([nrCentrum])
REFERENCES [dbo].[Centrum] ([nrCentrum])
	)
GO
CREATE TABLE [dbo].[stanyRealizacji](
	[nrRealizacji] [int] NOT NULL PRIMARY KEY Identity(1,1),
	[znaczenie] [varchar](max) NOT NULL,
	)
GO

CREATE TABLE [dbo].[Wysylki](
	[nrWysylki] [int] NOT NULL PRIMARY KEY Identity(1,1),
	[nrCentrum] [int] NOT NULL,
	[dataWysylki] [date] NOT NULL,
	CONSTRAINT [FK_Wysylki_Centrum] FOREIGN KEY([nrCentrum])
		REFERENCES [dbo].[Centrum] ([nrCentrum])
	)
GO

CREATE TABLE [dbo].[Szczepionki](
	[nrPartii] [int] NOT NULL PRIMARY KEY Identity(1,1),
	[liczbaDawek] [int] NOT NULL,
	[dataProdukcji] [nchar](10) NOT NULL,
	[nrFabryki] [int] NOT NULL,
	[nrFirmy] [int] NOT NULL,
	[nrWysylki] [int] NULL,
	CONSTRAINT [FK_Szczepionki_Fabryki] FOREIGN KEY([nrFabryki])
		REFERENCES [dbo].[Fabryki] ([nrFabryki]),
	CONSTRAINT [FK_Szczepionki_Firmy] FOREIGN KEY([nrFirmy])
		REFERENCES [dbo].[Firmy] ([nrFirmy]),
	CONSTRAINT [FK_Szczepionki_Wysylki] FOREIGN KEY([nrWysylki])
		REFERENCES [dbo].[Wysylki] ([nrWysylki])
	)
GO

CREATE TABLE [dbo].[Zlecenia](
	[nrZlecenia] [int] NOT NULL PRIMARY KEY Identity(1,1),
	[nrPacienta] [int] NOT NULL,
	[nrPartii] [int] NULL,
	[dataZlecenia] [date] NULL,
	[realizacja] [int] NOT NULL,
	[czyDrugie] [bit] NULL,
	 CONSTRAINT [FK_Zlecenia_Pacienci] FOREIGN KEY([nrPacienta])
		REFERENCES [dbo].[Pacienci] ([nrPacienta]),
	CONSTRAINT [FK_Zlecenia_stanyRealizacji] FOREIGN KEY([realizacja])
		REFERENCES [dbo].[stanyRealizacji] ([nrRealizacji]),
	CONSTRAINT [FK_Zlecenia_Szczepionki] FOREIGN KEY([nrPartii])
		REFERENCES [dbo].[Szczepionki] ([nrPartii])
	)
GO


-- W czasie wyszukiwania punktu dla klienta szukamy go po mieœcie
CREATE INDEX I_Centum_miasta ON Centrum (miasto)

-- Szukaj¹c odpowiedniego pacienta szukamy go po peselu, unique poniewa¿ pesel nie powinnien siê powtórzyæ
CREATE UNIQUE INDEX I_Pacienci_pesel ON Pacienci (pesel)

-- Wybieraj¹c kolejnych pacientów na szczepienia wybieramy po kategorii
CREATE INDEX I_Pacienci_kategoria ON Pacienci (kategoria)

-- Ka¿de z centrów bêdzie chcia³o szukaæ listê swoich pacientów
CREATE INDEX I_Pacienci_nrCentrum ON Pacienci (nrCentrum)

-- Chc¹c wysy³aæ szepionki do kolejnych punktów powinniœmy wysy³aæ najstarsze szczepionki ale zwracaj¹c uwagê na to z jakiej firmy pochodz¹ 
CREATE INDEX I_Szczepionki_dataProdukcji ON Szczepionki (dataProdukcji, nrFirmy)

-- Data zlecenia jest istotna kiedy punkt bêdzie chcia³ sprawdziæ zlecenia na przysz³y tydzieñ
CREATE INDEX I_Zlecenia_dataZlecenia ON Zlecenia (dataZlecenia)
