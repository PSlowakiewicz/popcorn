# OCR - from pdf to txt
The point of project was to create function that automatically take all files from directory (pdf only) recognise words and save text into files with the same name into another directory.
It uses tesseract as OCR dowloaded from  https://digi.bib.uni-mannheim.de/tesseract/ file 'tesseract-ocr-w64-setup-v5.0.0-alpha.20210811.exe'.
In code the adress of 'tesseract.exe' need to be changed.
The function 'fullsaver(dir_from, dir_to)' supposed to be used in order to save PDFs' text to .txt files.
- dir_from - directory that PDFs are supposed to be taken from
- dir_to - dirrectory that .txt file are supposed to be saved to

## Example
Bellow the are two files there testP2P.pdf is example of agreement and testP2P.txt is text from that file.

Example taken from https://konkursy.govtech.gov.pl/start/postepowanie/137
