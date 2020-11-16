%322CB Chiru Catalin-Mihail

:- use_module([check_predicates]).
:- use_module([tables]).

plus5(X,Y):- Y is X + 5.
make_format_str(MaxRowLen,Str) :- maplist(plus5,MaxRowLen,Rp), aux_format(Rp,Str).
aux_format([H],R) :- string_concat("~t~w~t~",H,R1), string_concat(R1,"+~n",R),!.
aux_format([H|T],R) :- string_concat("~t~w~t~",H,R1), string_concat(R1,"+ ",R2),
						aux_format(T,Rp), string_concat(R2,Rp,R).

%functie care pastreaza maximul pe fiecare element intre doua liste
%keepMaxes/3
keepMaxes([], [], []).
keepMaxes([], X, X).
keepMaxes(X, [], X).
keepMaxes([H1|T1], [H2|T2], R) :- H1 > H2, R = [H1|Rp], keepMaxes(T1, T2, Rp); R = [H2|Rp], keepMaxes(T1, T2, Rp). 

%parcurge linie cu linie tabela si actualizeaza maximele la momentul respectiv de timp
maxRowLen([], []).
maxRowLen([H|T], R) :- maplist(string_length, H, MaxH), maxRowLen(T,Rp), keepMaxes(MaxH, Rp, R), !.

%cu maxRowLen si make_format_str calculate o singura data, trecem prin recursie afisand fiecare linie
print_aux([], _).
print_aux([H|T], Str) :- format(Str, H), print_aux(T, Str).

%afiseaza un tabel pe baza cerintei
print_table_op([]).
print_table_op(Tbl) :- maxRowLen(Tbl, MaxLen), make_format_str(MaxLen, Str), print_aux(Tbl,Str), !.

%calculeaza pe baza lui op rezultatul lui T1 si T2 folosind maplist ca un zipWith, dupa care apendeaza capul de tabel
join_op(Op, NewCols, [_|T1], [_|T2], R) :- maplist(Op, T1, T2, Rez), append([NewCols], Rez, R).

%intoarce indexii celei de-a doua liste, din prima lista, pornind indexarea de la 0.
return_indexes([], _, _, []).
return_indexes(_, [], _, []).
return_indexes([H1|T1], [H2|T2], Idx, R) :- H1 == H2, R = [Idx|Rp], Idx2 is Idx+1, return_indexes(T1, T2, Idx2, Rp);
											Idx2 is Idx+1, return_indexes(T1, [H2|T2], Idx2, R).

%intoarce linia pe baza indecsilor calculati cu return_indexes.
return_line(_, [], []).
return_line(Line, [H|T], R) :- nth0(H, Line, Topic), R = [Topic|Rp], return_line(Line, T, Rp).

%intoarce lista de intrari ale tabelei conform coloanelor selectate
select_aux([], _, []).
select_aux([H|T], ListOfIndexes, R) :- return_line(H, ListOfIndexes, CrtLine), R = [CrtLine|Rp], select_aux(T, ListOfIndexes, Rp).

%returneaza tabela restransa la coloanele specificate in Cols
select_op([H|T], Cols, R) :- return_indexes(H, Cols, 0, ListOfIndexes), select_aux(T, ListOfIndexes,  Body), R = [Cols|Body].

%functia auxiliara prin care se filtreaza entries ale filter_op pe baza predicatului fara a lega permanent vars la o linie
%not nu este true daca interiorul este evaluat ca fals, deci predicatul nu se satisface, astfel moment stim ca linia respectiva trebuie
%eliminata din Query, altfel linia curenta trebuie pastrata
filter_aux([], _, _, []).
filter_aux([H|T], Vars, Pred, R) :- not((Vars = H, Pred)), filter_aux(T, Vars, Pred, R); R=[H|Rp], filter_aux(T,Vars, Pred, Rp).

%functia de filtrare ceruta
filter_op([H|T], Vars, Pred, R) :- filter_aux(T,Vars,Pred, Entries), R = [H|Entries].

%adauga liniei din movies ratingul corespunzator movie_idului
complex_query_aux(_, [], []) :- !.
complex_query_aux(Line, [H|T], R) :- nth0(0, Line, MovieId), nth0(2, H, UserMovieId), MovieId==UserMovieId, nth0(3, H, Rating) , append(Line, [Rating], R), !;
									complex_query_aux(Line, T, R), !.

%creeaza toate entrieurile posibile corespunzatoare complex_query2 (campurile din movies carora le este asociat ratingul)
complex_query_op([], _, []) :- !.
complex_query_op([H|T], Ent2, R) :- complex_query_aux(H, Ent2, Line), R =[Line|Rp], complex_query_op(T, Ent2, Rp), !.

%creeaza toata tabela movies + ratingurile aferente
complex_query2_op([H1|T1], [_|T2], R) :- append(H1, ["rating"], Header), complex_query_op(T1, T2, Entries), append([Header], Entries, R), !.

%posibile evaluri pentru queries
eval(table(Str), R) :- table_name(Str, R).
eval(tprint(Q), _) :- eval(Q, T), print_table_op(T).
eval(join(Pred, Cols, Q1, Q2), R) :- eval(Q1, T1),  eval(Q2, T2), join_op(Pred, Cols, T1, T2, R).
eval(select(Colums, Q), R) :- eval(Q, T), select_op(T, Colums,R).
eval(tfilter(S, G, Q), R) :- eval(Q, T), filter_op(T, S, G, R).
eval(complex_query1(Table), R) :- eval(Table, T), filter_op(T, [_, NAME, AA, PP, PC, PA, POO],
	((AA + PP > 12),(AA + PP + PC + PA + POO > 25), (sub_string(NAME, _, _, _,"escu"))), R).
eval(complex_query2(Genre, MinRating, MaxRating), R) :- table_name('movies', E1), table_name('ratings', E2), complex_query2_op(E1, E2, Aux1),
														filter_op(Aux1, [_, _, GENRE, _], (sub_string(GENRE, _, _, _, Genre)), Aux2),
														filter_op(Aux2, [_, _, _, RATING], ((RATING > MinRating - 1),(RATING < MaxRating + 1 )), R).
