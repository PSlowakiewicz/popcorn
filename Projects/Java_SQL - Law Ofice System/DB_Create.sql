SET NOCOUNT ON
USE master
if exists (select * from sysdatabases where name='kancelaria')
		drop database kancelaria
if exists (select * from sysobjects where id = object_id('sprawy'))
		drop table "sprawy"
if exists (select * from sysobjects where id = object_id('klienci'))
		drop table "klienci"

create database kancelaria;

create table kancelaria.dbo.klienci(
idk int not null primary key Identity(1,1),
imie varchar(20) not null,
nazwisko varchar(50) not null,
pesel varchar(11) null,
adres varchar(MAX) null,
miasto varchar(MAX) null,
kodPocztowy varchar(6) null check (kodPocztowy  like '__-___'),
nrTelefonu varchar(15) null
);

create table kancelaria.dbo.sprawy(
ids int not null primary key Identity(1,1),
sygnatura varchar(15) null,
pelnomoctnictwo bit not null,
idk int not null,
sad varchar(50) null,
wydzial varchar(30) null,
strona varchar(MAX) null,
cena money null,
zaplacone money null,
zakonczone bit null,
constraint "FK_Klienci_Sprawy" foreign key ("idk") REFERENCES kancelaria.dbo.klienci ("idk")
);

create table kancelaria.dbo.rozprawy (
idr int primary key Identity(1,1),
ids int not null,
dataRozprawy datetime not null,
nrSali varchar(5) null,
wynik varchar(MAX) null,
constraint "FK_Sprawy_Rozprawy" foreign key ("ids") references kancelaria.dbo.sprawy ("ids")
);

insert into kancelaria.dbo.klienci
(imie, nazwisko, pesel, adres, miasto, kodPocztowy, nrTelefonu)
values
	('Anna', 'Kruk', 64070676688, 'ul. Kurpiñskiego Karola 16','Kielce','24-431',539563700),
	('Wiktoria', 'Czerwinska', 71102125221, 'ul. Marsza³ka Józefa Pi³sudskiego 50', 'Kielce', '25-431',799523106),
	('Adrian', 'Borkowski', 79052455861, 'ul. £adna 14', 'Kielce', '25-431', 666992921),
	('Monika','Kelera','74082759796','ul. Pela 73/9','Starachowice','27-200','953027995'),
	('Marek','Kempa','98032314174','Borkowskiego 4','Starachowice','27-200','634561633');

insert into kancelaria.dbo.sprawy
(sygnatura, pelnomoctnictwo, idk, sad, wydzial, strona, cena, zaplacone, zakonczone)
values
	('I Ns 438/20', 1, 2, 'S¹d Rejonowy w Kielcach', 'I Wydzia³ Cywilny', 'Maciej Grucza³a', 3000, 1800, 0),
	('II W 2164/20', 1, 3, 'S¹d Rejonowy w Kielcach', 'II Wydzia³ Karny', null, 800, null, 1),
	('VIII Ns 946/19', 0, 1, 'S¹d Rejonowy w Kielcach', 'VIII Wydzia³ Cywilny','Zarz¹d Transportu Miejskiego', 1200, 1200, 0),
	('II W 235/21','1','4','S¹d Rejonowy w Starachowicach','II Wydzia³ Karny','Prokuratura w Starachowicach','7300',null,'0'),
    ('III RNs 59/21','1','4','S¹d Rejonowy w Starachowicach','III Wydzia³ Rodzinny','Prokuratura w Starachowicach','1000','1000','1');


insert into kancelaria.dbo.rozprawy
(ids, dataRozprawy, nrSali, wynik)
values
	(2, '2020-07-08', '105', 'Za porozumieniem stron'),
	(1,'2021-06-13 00:00:00.0','null','null'),
	('3','2022-01-03 00:00:00.0','null','Og³oszenie wyników'),
	('3','2021-05-10 13:15:00.0','3A','null'),
	('4','2021-05-10 15:50:00.0','321','Przes³uchanie œwiatków'),
	('5','2019-05-10 15:50:00.0','null','Opinia bieg³ego'),
	('5','2019-08-27 00:00:00.0','null','Zas¹denie kosztów');