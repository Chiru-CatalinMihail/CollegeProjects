%include "includes/io.inc"

extern getAST
extern freeAST

section .bss
    ; La aceasta adresa, scheletul stocheaza radacina arborelui
    root: resd 1

section .text
global main

final_result:
    push ebp
    mov ebp, esp
    mov ebx, [ebp+8] ;muta in ebx parametrul functiei, pointerul catre nod
    cmp ebx, 0x00 ;conditia de oprire a recursivitatii 
    je end_of_recursion ;daca un nod pointeaza la vid se opreste functia
    
    
    ;apel recursiv left->right-> current node(root), functioneaza in postordine
    mov ecx, [ebx+4] ;se muta in ecx pointer la fiul stang al nodului curent
    push ecx
    call final_result
    add esp, 4 ;se reface stiva
    mov ebx, [ebp+8] ;ebx se pierde prin recursivitate si atunci ii dam din nou valoarea trimisa ca parametru
    mov ecx, [ebx+8] ;se muta in ecx pointer la fiul drept al nodului curent
    push ecx
    call final_result
    add esp, 4
    
    mov ebx, [ebp+8] ;din nou ne asiguram ca ebx este pointer catre nodul curent
    mov ebx, [ebx] ;dereferentiem, obtinem pointer la inceputul structurii
    
    mov cl, [ebx] ;obtinem pointer la primul caracter din data
    
    ;comparatii cu toate semnele posibile *, /, +, -care fac jump la locul unde se calculeaza operatia
    cmp cl, '+'
    je adunare
    cmp cl, '*'
    je inmultire
    cmp cl, '/'
    je impartire
    cmp cl, '-'
    je minus ;daca dataul incepe cu - exista doua posibilitati sa fie operatia de scadere sau un numar negativ
    
    jmp atoi_pozitiv ;singurul caz in care se ajunge aici este daca exista data si nu are operatie in fata, deci implicit este un numar
    
    
minus:
    mov cl, [ebx+1]
    cmp cl, 0x00
    je scadere ;daca dupa - nu mai avem niciun caracter inseamna ca trebuie sa executam operatia de scadere
    jmp atoi_negativ ; altfel numarul este negativ si trebuie sa ii facem atoi


adunare:
    mov ebx, [ebp+8]
    mov ecx, [ebx+4]
    mov ecx, [ecx]
    mov eax, [ecx] ;muta cu ajutorul lui ecx informatia din nodul fiu stang in eax
    
    mov ecx, [ebx+8]
    mov ecx, [ecx]
    mov edx, [ecx] ;muta cu ajutorul lui ecx informatia din nodul fiu drept in edx
    add eax, edx   ;calculeaza operatia curenta intre cei doi fii
    mov ebx, [ebp+8]
    mov ebx, [ebx]
    mov [ebx], eax ;suprascrie data nodului curent cu rezultatul operatiei dintre copii
    jmp end_of_recursion   
    
scadere:
    ;analog adunarii
    mov ebx, [ebp+8]
    mov ecx, [ebx+4]
    mov ecx, [ecx]
    mov eax, [ecx] ;muta cu ajutorul lui ecx informatia din nodul fiu stang in eax
    
    mov ecx, [ebx+8]
    mov ecx, [ecx]
    mov edx, [ecx] ;muta cu ajutorul lui ecx informatia din nodul fiu drept in edx
    sub eax, edx   ;calculeaza operatia curenta intre cei doi fii
    mov ebx, [ebp+8]
    mov ebx, [ebx]
    mov [ebx], eax ;suprascrie data nodului curent cu rezultatul operatiei dintre copii
    jmp end_of_recursion  

inmultire:
    mov ebx, [ebp+8]
    mov ecx, [ebx+4]
    mov ecx, [ecx]
    mov eax, [ecx] ;muta cu ajutorul lui ecx informatia din nodul fiu stang in eax
    
    mov ecx, [ebx+8]
    mov ecx, [ecx]
    mov ecx, [ecx] ;muta informatia din nodul fiu drept in ecx, edx ia parte la procesul de inmultire
    
    xor edx, edx   ;ne asiguram ca nu avem reziduuri in edx
    imul ecx ;se inmulteste cu ecx
    mov ebx, [ebp+8]
    mov ebx, [ebx]
    mov [ebx], eax ;rezultatul inmultirii nu va depasi 4 octeti, deci se afla pastrat in intregime in eax, suprascriem data nodului curent cu rezultatul inmultirii
    jmp end_of_recursion 
    
impartire:
    mov ebx, [ebp+8]
    mov ecx, [ebx+4]
    mov ecx, [ecx]
    mov eax, [ecx] ;muta cu ajutorul lui ecx informatia din nodul fiu stang in eax
    
    mov ecx, [ebx+8]
    mov ecx, [ecx]
    mov ecx, [ecx] ;muta informatia din nodul fiu drept in ecx, edx ia parte la procesul de impartire
    
    xor edx, edx ;ne asiguram ca edx este gol
    cdq ;extindem pentru a cuprinde bitul de semn
    idiv ecx ;impartim la ecx
    mov ebx, [ebp+8]
    mov ebx, [ebx]
    mov [ebx], eax ;suprascrie data nodului curent cu rezultatul operatiei dintre copii
    jmp end_of_recursion 
    
atoi_pozitiv:
    ;xoreaza toti registrii folositi in construirea numarului
    xor edx, edx
    xor ecx, ecx
    xor eax, eax
for_atoi_poz: ;are echivalent in C: eax=0; for(i=0; i<=strlen(string);i++) eax=10*eax+(dl-'0');     unde in dl se gaseste cifra curenta sub forma de char
    mov dl, [ebx+ecx] ;caracterul curent este mutat in dl
    cmp dl, 0x00 ;daca se ajunge la null terminator se opreste procesul de construire al numarului
    je afara
    push ecx
    mov  ecx,10 ;folosim ecx pentru a inmulti cu 10 rezultatul
                ;cum eax si edx sunt implicati in inmultire si in ebx se pastreaza pointerul fata de care ne raportam,
                ;ecx este singurul registru cu care putem face calculul
    imul ecx
    pop ecx
    mov dl, [ebx+ecx] ;in urma inmultirii edx devine 0, pierdem informatia din dl, deci trebuie sa o reimprospatam
    sub dl, '0' ;calculam cifra raportandu-ne la charul '0'
    add eax, edx ;adunam la eax
    inc ecx
    jmp for_atoi_poz

atoi_negativ:
    xor edx, edx
    mov ecx, 1 ;se porneste iteratia prin string de la al 2-lea caracter primul fiind '-'
    xor eax, eax
    jmp for_atoi_poz

afara:
    xor ecx, ecx
    mov cl, [ebx] ; se verifica daca numarul trecut prin atoi a fost pozitiv sau negativ
    cmp cl, '-' ;daca a fost negativ se inmulteste cu '-1', daca nu se continua
    je pune_minus
nr_e_negativ_acum:
    mov [ebx], eax ;se trece in data in locul stringului valoarea reala a numarului
    jmp end_of_recursion
    
pune_minus:
    mov ecx, -1
    imul ecx
    jmp nr_e_negativ_acum ;se intoarce la loc in functie cu amendamentul ca daca numarul avea '-' la inceput ca string acum este numar negativ
    
end_of_recursion:
    ;iese din recursivitate
    leave
    ret


main:
    mov ebp, esp; for correct debugging
    ; NU MODIFICATI
    push ebp
    mov ebp, esp
    
    ; Se citeste arborele si se scrie la adresa indicata mai sus
    call getAST
    mov [root], eax
     
    
    ; Implementati rezolvarea aici:
    push eax ;ii trimit functiei pointer la primul nod
    call final_result
    mov esp, ebp
    ;dataul din root este acum rezultatul tuturor calculelor
    mov eax, [root] ;adresa root
    mov eax, [eax]  ;root
    mov eax, [eax]  ;sectiunea data din root conform structurii de tip node
    PRINT_DEC 4, eax 
    
    
    ; NU MODIFICATI
    ; Se elibereaza memoria alocata pentru arbore
    push dword [root]
    call freeAST
    
    xor eax, eax
    leave
    ret
