begin transaction
use MagazynSczepionek
exec dodaj 7, 5,5, '2021-09-01'	
exec dodaj 6, 1,1, '2021-06-01'
exec dodaj 1, 1,1, '2021-05-18';

rollback transaction