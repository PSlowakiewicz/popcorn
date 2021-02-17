relatedArtists <- read.csv("Data/relatedArtists.csv", stringsAsFactors = F, fileEncoding = "latin1")
relatedArtists <- relatedArtists[-1]

artistsNames <- read.csv("Data/artistsNames.csv", stringsAsFactors = F, fileEncoding = "latin1")
artistsNames <- artistsNames[-1]

artistsId <- read.csv("Data/artistsId.csv", stringsAsFactors = F, fileEncoding = "latin1")
artistsId <- artistsId[-1]

allGenres <- read.csv("Data/allGenres.csv", stringsAsFactors = F, fileEncoding = "latin1")
allGenres <- allGenres[-1]

allGenresPatryk <- read.csv("Data/allGenresPatryk.csv", stringsAsFactors = F, fileEncoding = "latin1")
allGenresPatryk <- allGenresPatryk[-1]
allGenresTomek <- read.csv("Data/allGenresTomek.csv", stringsAsFactors = F, fileEncoding = "latin1")
allGenresTomek <- allGenresTomek[-1]
allGenresDominik <- read.csv("Data/allGenresDominik.csv", stringsAsFactors = F, fileEncoding = "latin1")
allGenresDominik <- allGenresDominik[-1]