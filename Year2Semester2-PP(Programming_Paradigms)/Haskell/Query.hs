module Query where

import UserInfo
import Rating
import Movie
import Data.List.Split

type Column = String
type TableSchema = [Column]
type Field = String
type Entry = [Field]

data Table = Table TableSchema [Entry]

type ColSeparator = Char
type LnSeparator = Char

-- TODO 1
--Eliminam la citire ultimul element din lista mare de entryuri fiindca va fi ""
read_table :: ColSeparator -> LnSeparator -> String -> Table
read_table col line str = Table (generateTableSchema col line str)
                                (destroyLast $ generateEntries col line str)
                where destroyLast l = (take  ((length l)-1) l)

user_info = read_table '|' '\n' user_info_str
rating = read_table ' ' '\n' rating_str
movie = read_table '|' '\n' movie_str

--functie care face spargerea unui string pe separatorul de linii
lineSpliter :: LnSeparator -> String -> Entry
lineSpliter line str = splitOn [line] str

--functie care face spargerea unui string pe separatorul de coloane
columnSpliter :: ColSeparator -> String -> Entry
columnSpliter col str = splitOn [col] str

-- functie care genereaza TableSchema pentru tabela
generateTableSchema :: ColSeparator -> LnSeparator -> String -> TableSchema
generateTableSchema col line str = columnSpliter col $ head $ lineSpliter line str

-- functie care genereaza restul tabelei, adica lista de intrari
generateEntries :: ColSeparator -> LnSeparator -> String -> [Entry]
generateEntries col line str = map (columnSpliter col) $ tail $ lineSpliter line str

-- TODO 2
instance Show Table where
    show (Table h e) = showing (Table h e) (overallMax (Table h e)) ((length h)+1)

-- adauga padding daca stringul are dimensiune mai mica decat stringul maxim
addPadding :: Int -> String -> String
addPadding max str = if (length str) == max then str
                                            else str ++ (take (max - (length str)) $ repeat ' ')

-- genereaza bordurile tabelei adaugand toate lungimile maxime ale unui Field
-- plus cate delimitatoare de coloana sunt
createBorders :: [Int] -> Int -> String
createBorders lMaxime nrColDel = (take ((foldr (+) 0 lMaxime) + nrColDel) $ repeat '-') ++ "\n"

-- initializarea maximelor
initMax :: TableSchema -> [Int]
initMax header = map length header

--intoarce maximele totale corespunzatoare fiecaruifield
overallMax (Table header entries) = foldr (\x y -> zipWith (max) (map length x) y) (initMax header)
                                                                                           entries

user_dimensions = overallMax user_info
rating_dimensions = overallMax rating
movie_dimensions = overallMax movie

--afiseaza o celula corespunzatoare unei coloane
displayCol :: String -> [Int] -> Int -> String
displayCol str lMaxime index = "|"++(addPadding (lMaxime !! (index)) str)

--afiseaza o linie corespunzator
displayEntry :: [String] -> [Int] -> String
displayEntry l lMaxime = snd (foldr (\x (y,z)-> (y-1, (displayCol x lMaxime (y-1)) ++ z))
                                                                ((length l),[]) l) ++ "|\n"

--intreaga implementare a show pentru a beneficia de lazy implementation
showing :: Table -> [Int] -> Int -> String
showing (Table h e) lMaxime nrColDel = (createBorders lMaxime nrColDel) ++
                            (displayEntry h lMaxime) ++
                            (createBorders lMaxime nrColDel) ++
                            (foldr (\x y -> (displayEntry x lMaxime)++y) "" e) ++
                            (createBorders lMaxime nrColDel)

                       
--returneaza entriurile din tabela
returnEntries :: Table -> [Entry]
returnEntries (Table h e) = e

--returneaza headerul tabelei
returnHead :: Table -> TableSchema
returnHead (Table h e) = h

data FilterCondition = Lt Field Integer | Eq Field String | In Field [String] | Not FilterCondition

-- TODO 3
getFilter :: FilterCondition -> TableSchema -> (Entry -> Bool)
getFilter (Lt field val) h = (\x -> (read (x!!(returnIndex field h)) :: Integer) < val)
getFilter (Eq field str) h = (\x -> (x!!(returnIndex field h)) == str)
getFilter (In field strs) h = (\x -> (x!!(returnIndex field h)) `elem` strs)
getFilter (Not filter) h = not . (getFilter filter h)

-- TODO 4
data Query = Filter FilterCondition Query |  
             Select [String] Query |
             SelectLimit [String] Integer Query |
             Cosine Query |
             Query :|| Query |
             Atom Table

eval :: Query -> Table
eval (Atom t) = t
eval (Select fields (Atom t)) = constructNewTable fields t
eval (SelectLimit fields idx (Atom t)) = trimTable (constructNewTable fields t) idx
eval (Select fields q) = eval (Select fields (Atom (eval q)))
eval (Filter fc (Atom (Table h e))) = (Table h (filter (getFilter fc h) e))
eval (Filter fc q) = eval (Filter fc (Atom(eval q)))
eval (q1 :|| q2) = (eval q1) `mergeTables` (eval q2)
eval (Cosine q) = undefined

-- eval (Cosine (Atom t)) = sortById t
-- eval (Cosine q) = eval (Cosine (Atom(eval q)))

--reduce entryurile din tabela pe baza limitarii selectate
trimTable :: Table -> Integer -> Table
trimTable (Table h e) idx = (Table h (take (fromIntegral idx) e))


--construieste o noua tabela pe baza campurilor selectate din entry
constructNewTable :: Entry -> Table -> Table
constructNewTable l (Table h e) = foldr 
                                (\x y -> lazyfoldConstruct y (Table h e) (returnIndex x h))
                                (Table [] []) l

--concateneaza 2 tabele
concatTables :: Table -> Table -> Table
concatTables (Table [] []) t = t
concatTables (Table h1 e1) (Table h2 e2) = Table (h2++h1) (zipWith (\x y -> y++x) e1 e2)

--reuniunea a doua tabele cu TableSchema identica este o tabela cu entries appendate
mergeTables :: Table -> Table -> Table
mergeTables (Table h1 e1) (Table h2 e2) = Table h1 (e1++e2)


--intoarce indexul reprezentand prima aparitie a unui String in TableSchema
returnIndex :: String -> TableSchema -> Int
returnIndex str l =  snd $ head $ filter ((str==) . (fst)) $ zip l [0..]

--creeaza un nou field , pornind de la tabela principala pe baza unui index dat
createField :: Table -> Int -> TableSchema
createField (Table h e) idx = [h!!idx] 

--creeaza intrarile pentru noul field , pornind de la tabela principala pe baza unui index dat
createEntry :: Table -> Int -> [Entry]
createEntry (Table h e) idx = foldr (\x y -> [[x!!idx]] ++ y) [] e

--evalueaza lenes functia data lui foldr pentru constructor
lazyfoldConstruct :: Table -> Table -> Int -> Table
lazyfoldConstruct t query_table index = concatTables t (Table (createField query_table index)
                                                              (createEntry query_table index))


--Bonus intoarce tabela sortata crescator dupa id-ul userului
sortById :: Table -> Table
sortById (Table h e) = (Table h (quicksort e 0))

--sorteaza crescator dupa indexul i dat
quicksort:: [Entry] -> Int -> [Entry]
quicksort [] _ = []
quicksort (x:xs) idx = (quicksort [y | y <- xs, (read (y!!idx):: Int) <= (read (x!!idx):: Int)] idx) ++ [x] ++ (quicksort [y | y <- xs, (read (y!!idx):: Int) > (read (x!!idx):: Int)] idx)

-- --creeaza tabela ceruta la bonus
-- createCosineQuery:: Table -> Table
-- createCosineQuery (Table h e) = (Table ["user_id", "movie_id", "rating"] (createBonusEntries e))

-- --creaza entriurile conform cu bonusul
-- createBonusEntries :: [Entry] ->  -> [Entry]
-- createBonusEntries

-- TODO 5
--cauta zone-ul lui user_id dupa care filtreaza din user_info toti ceilalti useri cu acest zone
--si selecteaza doar campurile "user_id" si "occupation"
same_zone :: String -> Query
same_zone user_id = Atom $  eval $ 
                    Select ["user_id", "occupation"] $
                    Filter (Not $ Eq "user_id" user_id)
                    (Filter (Eq "zone" $ head $ head $ returnEntries $ eval $
                    Select ["zone"] $ Filter (Eq "user_id" user_id) (Atom user_info))
                    (Atom user_info))

male_within_age :: Integer -> Integer -> Query
male_within_age minAge maxAge = Atom $ eval $ 
                                Select ["occupation","zone"] $
                                Filter (Eq "sex" "M") $
                                Filter (Lt "age" maxAge) $
                                Filter (Not (Lt "age" (minAge + 1))) 
                                (Atom user_info)

mixed :: [String] -> [String] -> Int -> Query
mixed zones occupations threshold = Atom $ eval $
                                   Select ["user_id"] $
                                   Filter (In "zone" zones) $
                                   Filter (In "occupation" occupations) $
                                   Filter (Lt "age" (toInteger threshold)) (Atom user_info)