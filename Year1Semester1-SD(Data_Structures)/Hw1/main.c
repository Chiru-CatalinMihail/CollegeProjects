#include <string.h>
#include <stdio.h>
#include "structuri.h"

#define A 10
#define D 11
#define RV 12
#define U 13
#define S 14
#define I 15
#define RC 16
#define CS 17
#define B 18
#define T 19
#define P 20
#define CN 21

#define N 200
#define M 70

/*deoarce compilatorul nu imi recunoaste functia strdup, mi-am creat o functie
ce are aceeasi functionalitate*/
char* mystrdup(const char *str)
{
    size_t len = strlen(str);
    char *result = malloc(len + 1);
    if (!result)
    {
    	printf("Nu s-a putut aloca sirul\n");
    	return NULL;
    }
    memcpy(result, str, len + 1);
    return result;
}

//aloca un pointer in care concateneaza alte doua
char* myconcat(const char *s1, const char *s2)
{
    char *result = malloc(strlen(s1) + strlen(s2) + 1); 
    if (!result)
    {
    	printf("Nu s-a putut aloca sirul\n");
    	return NULL;
    }
    strcpy(result, s1);
    strcat(result, s2);
    return result;
}

//transforma literele semnificative de comanda in comanda sub forma de int
int cmd_to_int(char el1, char el3)
{
	switch(el1)
	{
      case 'a' :
         return A;
      case 'd' :
      	return D;
      case 'u' :
      	return U;
      case 's' :
      	return S;
      case 'i' :
         return I;
      case 'b' :
      	return B;
      case 't' :
      	return T;
      case 'p' :
      	return P;
      case 'r' :
      	if (el3=='v')
      	{
      		return RV;
      	}
      	return RC;
      case 'c' :
      	if (el3=='s')
      	{
      		return CS;
      	}
      	return CN;
      default :
         return -2;
   }
}

//aloca si initializeaza pozitia 0 a LSC
TLL InitLC(char OK)
{
  TLL aux;
  aux= (TLL) malloc(sizeof(TCL));
  if (!aux)
  {
    printf("eroare alocare sublista\n");
    return NULL;
  }
  if (OK==1)
  {
  	aux->index=0;
  	aux->urm=NULL;
  	aux->card= NULL;
  }
  return aux;
}

//aloca un element (lista de un element) de tip LC
TLL AlocaLC(TLC card, int pozitie)
{
	TLL aux=(TLL)malloc(sizeof(TCL));
	if (!aux)
	{
		printf("eroare alocare sublista\n");
    	exit (-2);
	}
	aux->card=card;
	aux->index=pozitie;
	aux->urm=NULL;
	return aux;
}

//aloca un card cu informatia corespunzatoare
TLC AlocaCard(char* card_number, char* pin, char* exp_date,
			char* cvv, char* status)
{
	TLC caux=(TLC)malloc(sizeof(TCC));
	if (caux==NULL)
	{
		printf("eroare alocare card\n");
    	exit (-2);
	}
	caux->card_number=card_number;
	caux->pin=pin;
	caux->pin_tries=0;
	caux->exp_date=exp_date;
	caux->cvv=cvv;
	caux->balance=0;
	caux->status=status;
	caux->history=NULL;
	caux->urm=NULL;

	return caux;
}

//aloca o celula de istoric cu informatia corespunzatoare
TLH AlocaHist(char* info)
{
	TLH aux=(TLH)malloc(sizeof(TCH));
	if (!aux)
	{
		printf("eroare alocare istoric\n");
    	return NULL;
	}
	aux->info_h=info;
	aux->urm=NULL;
	return aux;
}

//introduce intr-un card o celula de istoric
TLC InserareCelHist(TLC card, char* info)
{
	TLH aux=AlocaHist(info);
	if (aux==NULL)
	{
		printf("alocarea istoricului a esuat\n");
		return NULL;
	}
	//daca nu are deloc istoric celula pe care o primeste devine istoricul
	if (card->history==NULL)
	{
		card->history=aux;
		return card;
	}
	aux->urm=card->history;
	card->history=aux;
	return card;
}

//stergand celula cu celula elimina tot istoricul unui card
void DistrugeIstoric(TLC card)
{
	TLH p=NULL;
	while(card->history!=NULL)
	{
		p=card->history;
		card->history=p->urm;
		p->urm=NULL;
		free(p->info_h);
		free(p);
	}
}

//distruge elementele ce tin de informatia care se afla intr-un card
void DistrElem(char* card_number, char* pin, char* exp_date, char* cvv,
						char* status)
{
	free(card_number);
	free(pin);
	free(exp_date);
	free(cvv);
	free(status);
}

//elibereaza un card
void DistrUnCard(TLC p)
{
	DistrugeIstoric(p);
	DistrElem(p->card_number, p->pin, p->exp_date,p->cvv, p->status);
	p->urm=NULL;
	free(p);
}

/*pentru reverse transaction cauta cu ajutorul strstr celula care contine
sursa si destinatia de la tranzactia efectuata, dupa care elimina celula 
respectiva cu totul din lista*/
TLC DistrHistRev(TLC card, char* source)
{
	TLH p=card->history, aux=NULL;
	if(strstr(p->info_h, source))
	{
		card->history=p->urm;
		p->urm=NULL;
		free(p->info_h);
		free(p);
		return card;
	}
	aux=card->history->urm;
	while(aux)
	{
		if (strstr(aux->info_h, source))
		{
			p->urm=aux->urm;
			aux->urm=NULL;
			free(aux->info_h);
			free(aux);
			return card;
		}
		p=aux;
		aux=aux->urm;
	}
	return NULL;
}

//distruge card cu card tot ce se afla intr-o sublista
void DistrCarduriDinLista(TLL LSC)
{
	TLC p=NULL;
	while(LSC->card!=NULL)
	{
		p=LSC->card;
		LSC->card=p->urm;
		p->urm=NULL;
		DistrUnCard(p);
	}
}

//elibereaza memoria tuturor sublistelor
void DistrToateLSC(ALL alsc)
{
	TLL p=NULL;
	while(*alsc!=NULL)
	{
		p=(*alsc);
		(*alsc)=p->urm;
		p->urm=NULL;
		DistrCarduriDinLista(p);
		free(p);
	}
}

//calculeaza conform ipotezei indexul listei in care se afla cardul
int poz_card(char* a, int nr_max)
{
	int s=0, cifra=0;
	unsigned int i;
	for (i = 0; i < 16 ; i++)
	{
		cifra=a[i]-'0';
		s+=cifra;
	}
	return (s%nr_max);
}

//verifica daca pinul are o forma corecta, 4 elemente, toate fiind cifre
int verify_pin(char* pin)
{
	int i=0;
	if (strlen(pin)!=4)
	{
		return 0;
	}
	for (i = 0; i < 4; ++i)
	{
		if (((pin[i]-'0')<0)||((pin[i]-'0')>9))
		{
			return 0;
		}
	}
	return 1;
}

//pentru adaugarea unui card verifica daca mai exista vreunul cu acelasi numar
int verifica_identic(TLL lista_potrivita, char* c_num)
{
	TLC aux=lista_potrivita->card;
	if (aux==NULL)
	{
		return 0;
	}
	while(aux)
	{
		if (strcmp(aux->card_number, c_num)==0)
		{
			return 1;
		}
		aux=aux->urm;
	}
	return 0;
}

/*avand in vedere problemele cu scurgerile de memorie am creat
aceasta functie pentru a verifica duplicitatea cardului, de 
dinainte de a aloca memoria*/
int conditie_duplicat(TLL toata_l, char* c_num, int nr_max)
{
	int exista=0, poz=poz_card(c_num, nr_max);
	TLL aux=toata_l;
	if (aux->urm==NULL)
	{
		exista=verifica_identic(aux, c_num);
		if (exista==1)
		{
			return exista;
		}
	}
	while(aux)
	{
		if (aux->index==poz)
		{
			exista=verifica_identic(aux, c_num);
			return exista;
		}
	aux=aux->urm;
	}
	return exista;
}

//gaseste cardul de dinaintea celui de interes 
//(predecesorul celui care va fi sters)
TLC gaseste_precedent(TLL lista_potrivita, char* c_num, int *OK)
{
	TLC aux=lista_potrivita->card;
	if (strcmp(lista_potrivita->card->card_number, c_num)==0)
	{
		return aux;
	}

	while(aux->urm)
	{
		if (strcmp(aux->urm->card_number, c_num)==0)
		{
			(*OK)++;
			return aux;
		}
		aux=aux->urm;
	}
	return NULL;
}

//gaseste cardul cautat intr-o lista potrivita
TLC gaseste_card(TLL lista_potrivita, char* c_num)
{
	TLC aux=lista_potrivita->card;
	while(aux)
	{
		if (strcmp(aux->card_number, c_num)==0)
		{
			return aux;
		}
		aux=aux->urm;
	}
	return NULL;
}

//gaseste lista potrivita in care trebuie cautat un card
TLL gaseste_LSC(TLL x, char* c_num, int nr_max)
{
	TLL aux=x;
	int poz=0, i=x->index;
	poz=poz_card(c_num, nr_max);
	
	if (poz==0)
	{
		return aux;
	}

	for (; i < poz; ++i)
	{
		aux=aux->urm;
	}
	
	return aux;
}

/* cauta lista in care trebuie adaugat cardul si daca aceasta nu exista
			creeaza liste pana la ea */
TLL add_lc_card_pe_poz(TLL lc, char* card_number, char* pin, char* exp_date,
			char* cvv, char* status, int nr_max, int *nr_crt)
{
	int OK=0, poz_max_crt, pozitie, i;
	TLL aux=lc, p=NULL, k=NULL;
	TLC cadg=NULL;
	pozitie=poz_card(card_number, nr_max);
	while(aux)
	{
		if (aux->index==pozitie)
		{
			OK=1;
			cadg=AlocaCard(card_number, pin, exp_date, cvv, status);
			if (cadg==NULL)
			{
			DistrElem(card_number, pin, exp_date, cvv, status);
			exit (-2);
			}

			cadg->urm=aux->card;
			aux->card=cadg;
			(*nr_crt)++;
			return lc;
			
		}
		if (aux->urm==NULL)
		{
			poz_max_crt=aux->index;
			k=aux;
		}
		aux=aux->urm;
	}

	if (OK==0)
	{
		for ( i = poz_max_crt+1; i < pozitie; ++i)
		{
			p=AlocaLC(NULL, i);
			if (!p)
			{
				DistrToateLSC(&aux);
				return NULL;
			}
			k->urm=p;
			k=k->urm;
		}
		cadg=AlocaCard(card_number, pin, exp_date, cvv, status);
		if (cadg==NULL)
		{
			DistrElem(card_number, pin, exp_date, cvv, status);
			exit (-2);
		}
		p=AlocaLC(cadg, pozitie);
		if (!p)
			{
				DistrToateLSC(&aux);
				exit (-2);
			}
		(*nr_crt)++;
		k->urm=p;
	}
	return lc;
}

/* cauta lista in care trebuie mutat cardul pentru redimensionare si daca
aceasta nu exista creeaza liste pana la ea, similar cu add_lc_pe_poz */
TLL redim_card_pe_poz(TLL lc, TLC cadg, int nr_max)
{
	int OK=0, poz_max_crt, pozitie, i;
	TLL aux=lc, p=NULL, k=NULL;

	pozitie=poz_card(cadg->card_number, nr_max);
	
	while(aux)
	{
		if (aux->index==pozitie)
		{
			OK=1;
			cadg->urm=aux->card;
			aux->card=cadg;
			return lc;
		}
		if (aux->urm==NULL)
		{
			poz_max_crt=aux->index;
			k=aux;
		}
		aux=aux->urm;
	}

	if (OK==0)
	{
		for ( i = poz_max_crt+1; i < pozitie; ++i)
		{
			p=AlocaLC(NULL, i);
			if (!p)
			{
				DistrToateLSC(&aux);
				return NULL;
			}
			k->urm=p;
			k=k->urm;
		}
		
		p=AlocaLC(cadg, pozitie);
		if (!p)
			{
				DistrUnCard(cadg);
				exit (-2);
			}
		k->urm=p;
	}
	return lc;
}

/*verifica daca suma este multiplu de 10 si scrie in output specific
 fiecarei cerinte*/
int multiple_of_ten(char* sum, FILE *fp, int cmd)
{
	int suma=atoi(sum);
	if (suma%10!=0)
	{
		switch(cmd)
		{
			case RC:
				{
				fprintf(fp, "The added amount must be multiple of 10\n");
				break;
				}
			case CS:
				{
				fprintf(fp, "The requested amount must be multiple of 10\n");
				break;
				}
			case T:
				{
				fprintf(fp, "The transferred amount must be multiple of 10\n");
				break;
				}
			case RV:
				break;

		}
	}

	return suma;
}

//scrie in output tot istoricul corespunzator unui card
void scrie_hist(TLC card, FILE *fp)
{
	TLH aux=card->history;
	if (aux==NULL)
	{
		return;
	}

	while(aux)
	{
		fprintf(fp, "(%s)", aux->info_h);
		if (aux->urm!=NULL)
		{
			fprintf(fp, ", ");
		}
		aux=aux->urm;	
	}
	return;
}

//folosita pentru functia show in afisarea intregii baze de date cu carduri
void AfiseazaLC(TLL lista_potrivita, FILE *fp)
{
	TLC p=lista_potrivita->card;
	//cazul in care sublista este vida
	if (p==NULL)
	{
		fprintf(fp, "pos%d: []\n", lista_potrivita->index);
		return;
	}

	//cazul in care sublista contine elemente
	fprintf(fp, "pos%d: [\n", lista_potrivita->index);
	while (p)
	{
		
		fprintf(fp, "(card number: %s, PIN: %s, expiry date: %s, CVV: %s," ,
				p->card_number, p->pin, p->exp_date, p->cvv);
		fprintf(fp, " balance: %d, status: %s, history: [", 
 				p->balance, p->status);
		scrie_hist(p, fp);
		fprintf(fp,"])\n");
		p=p->urm;
	}
	fprintf(fp, "]\n");
	return;
}

//muta legaturile intr-o lista noua, si elibereaza vechiul schelet
TLL redimensioneaza(TLL x, int nr_max)
{
	TLL aux=NULL, pivot=x;
	TLC card=NULL;
	aux=InitLC(1);
	if (!aux)
	{
		exit(-2);
	}
  	
	while(pivot)
	{
		while(pivot->card)
		{
			card=pivot->card;
			pivot->card=card->urm;
			card->urm=NULL;
			aux=redim_card_pe_poz(aux, card, nr_max);
		}

		pivot=pivot->urm;
	}
	DistrToateLSC(&x);
	return aux;
}

//adauga un card in baza de date conform cerintei
TLL add_card(TLL x, char *linie, int *nr_max, int *nr_crt, FILE *fp)
{
	char *token=NULL, *card_number=NULL, *pin=NULL, *exp_date=NULL, *cvv=NULL, *status=NULL;
	int exista=0;
	/*spargerea liniei de input in comenzi folosind tokeni,
	voi repeta acest algoritm pe tot parcursul temei*/
	token=strtok(linie, " ");
	token=strtok(NULL, " ");
	exista=conditie_duplicat(x, token, (*nr_max));
	if (exista==1)
	{
		fprintf(fp, "The card already exists\n");
		return x;
	}
	/*daca nu exista cardul in baza de date intai se va verifica daca este 
	cazul sa se redimensioneze baza de date pentru a-i face loc*/
	if ((*nr_crt)==(*nr_max))
	{
		(*nr_max)=2*(*nr_max);
		x=redimensioneaza(x, (*nr_max));
	}
	card_number=mystrdup(token);
	if (!card_number)
	{
		exit(-2);
	}

	token=strtok(NULL, " ");
	pin=mystrdup(token);
	if (!pin)
	{
		free(card_number);
		exit(-2);
	}

	token=strtok(NULL, " ");
	exp_date=mystrdup(token);
	if (!exp_date)
	{
		free(card_number);
		free(pin);
		exit(-2);
	}
	
	token=strtok(NULL, "\n");
	cvv=mystrdup(token);
	if (!cvv)
	{
		free(card_number);
		free(pin);
		free(exp_date);
		exit(-2);
	}
	
	token="NEW";
	status=(char *)malloc(7*sizeof(char));
	if (!status)
	{
		free(card_number);
		free(pin);
		free(exp_date);
		free(cvv);
		exit(-2);
	}
	strcpy(status, token);

	x=add_lc_card_pe_poz(x, card_number, pin, exp_date, cvv,
						status, (*nr_max), nr_crt);
	if (x==NULL)
	{
		DistrElem(card_number, pin, exp_date, cvv, status);
		exit(-2);
	}
	return x;
}

void show(TLL x, char *linie, int nr_max, int exists, FILE *fp)
{
	TLL aux=x;
	TLC p=NULL;
	int poz, i;
	char *token, *card_number;
	
	//daca nu exista niciun element in niciun lsc nu se va afisa nimic	
	if ((x->urm==NULL)&&(x->card==NULL)&&(x->index==0)&&(exists==0))
	{
		return;
	}
	
	token=strtok(linie, " \n");
	token=strtok(NULL, "\n");
	
	//daca mai exista informatie dupa show se va arata un card specific
	if (token!=NULL)
	{
		card_number= mystrdup(token);
		if (!card_number)
		{
			exit(-2);
		}
		poz=poz_card(card_number, nr_max);
		for (i = aux->index; i < poz; ++i)
		{
			aux=aux->urm;
		}
		p=gaseste_card(aux, card_number);
		fprintf(fp, "(card number: %s, PIN: %s, expiry date: %s, CVV: %s," ,
				p->card_number, p->pin, p->exp_date, p->cvv);
		fprintf(fp, " balance: %d, status: %s, history: [", 
 				p->balance, p->status);
		scrie_hist(p, fp);
		fprintf(fp, "])\n");
		free(card_number);
		return;
	}else
		{
			while(aux)
			{
				AfiseazaLC(aux, fp);
				aux=aux->urm;
			}

		}
}

TLL del_card(TLL x, char *linie, int nr_max, int *nr_crt, int *exists)
{
	TLL aux=x;
	TLC p=NULL, elimina=NULL;
	int OK=0, poz=0;
	char *token=NULL, *card_number=NULL;

	token=strtok(linie, " \n");
	token=strtok(NULL, "\n");
	card_number=mystrdup(token);
	if (card_number==NULL)
	{
		exit(-2);
	}

	poz=poz_card(card_number, nr_max);
	if (poz!=0)
	{
		while(aux)
		{
			if (aux->index==poz)
			{
			break;
			}
			aux=aux->urm;
		}
	}

	/*gaseste cardul de dinaintea celui care trebuie sters pentru a putea 
	efectua stergerea*/	
	p=gaseste_precedent(aux, card_number, &OK);
	if(p==NULL)
	{
		free(card_number);
		exit (-2);
	}
	if (OK==0)
	{
		aux->card=p->urm;
		p->urm=NULL;
		DistrUnCard(p);
	}else
		{
		elimina=p->urm;
		p->urm=elimina->urm;
		elimina->urm=NULL;
		DistrUnCard(elimina);
		}
	(*nr_crt)--;
	(*exists)=1;
	free(card_number);
	return x;
}

//insereaza cardul
void insert(TLL x, char *linie, int nr_max, FILE *fp)
{
	TLL aux=NULL;
	TLC p=NULL;

	char *token=NULL, *card_number, *pin, *comanda=NULL;
	comanda=mystrdup(linie);

	if (comanda==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}

	/*nul terminatorul va trebui mutat o pozitie mai in spate deoarce linia 
	intreaga contine un \n mereu la final, aceasta abordare va fi folosita
	ulterior in mai multe functii*/
	comanda[strlen(comanda)-1]='\0';
	token=strtok(linie, " \n");
	
	// tokenul este acum pozitionat pe numarul cardului
	token=strtok(NULL, " ");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		exit(-2);
	}
	
	//gaseste cu cele 2 functii cardul in intreaga baza de date
	aux=gaseste_LSC(x, card_number, nr_max);
	p=gaseste_card(aux, card_number);
	free(card_number);
	if (p==NULL)
	{
		printf("Nu exista cardul in baza de date (insert)\n");
		free(comanda);
		exit(-2);
	}

	//daca statusul este locked, operatia va esua
	if (strcmp(p->status,"LOCKED")==0)
	{
		//concateneaza cu valoarea de adevar a comenzii, instructiunea initiala
		linie=myconcat("FAIL, ", comanda);
		if (linie==NULL)
		{
			free(comanda);
			exit(-2);
		}
		fprintf(fp, "The card is blocked.");
		fprintf(fp," Please contact the administrator.\n");
		//adauga in istoric informatia corespunzatoare, inserand la inceput
		//aceasta abordare va fi folosita in toate functiile care urmeaza
		free(comanda);
		p=InserareCelHist(p, linie);
		if (p==NULL)
		{
			free(linie);
			exit(-2);
		}
		return;
	}
	
	//tokenul este acum pozitionat pe pin-ul din comanda
	token=strtok(NULL, "\n");
	pin=mystrdup(token);
	if (!pin)
	{
		free(comanda);
		exit(-2);
	}
	//la fiecare greseala creste numarul de erori si la 3 blocheaza cardul
	if (strcmp(p->pin, pin)!=0)
	{
		free(pin);
		p->pin_tries++;
		fprintf(fp, "Invalid PIN\n");
	  	if (p->pin_tries==3)
		{
			strcpy(p->status,"LOCKED");
			fprintf(fp, "The card is blocked.");
			fprintf(fp," Please contact the administrator.\n");
		}
		//concateneaza comanda data cu FAIL/SUCCESS daca a reusit sau nu
		linie=myconcat("FAIL, ", comanda);
		if (linie==NULL)
		{
			free(comanda);
			exit(-2);
		}

		free(comanda);
		p=InserareCelHist(p, linie);
		if (p==NULL)
		{
			free(linie);
			exit(-2);
		}
		return;
	}
	free(pin);
	//verifica daca pinul este cel initial
	if(strcmp(p->status, "NEW")==0)
	{
		fprintf(fp, "You must change your PIN.\n");
	}
	linie=myconcat("SUCCESS, ", comanda);
		if (linie==NULL)
		{
			free(comanda);
			exit(-2);
		}
		free(comanda);
		p=InserareCelHist(p, linie);
		if (p==NULL)
		{
			free(linie);
			exit(-2);
		}

	/*daca totul s-a executat cu succes reseteaza numarul 
	de incercari ale pinului*/
	p->pin_tries=0;
}

//deconecteaza cardul
void cancel(TLL x, char *linie, int nr_max)
{
	TLL aux=NULL;
	TLC p=NULL;

	char *token=NULL, *comanda=NULL, *card_number=NULL;
	comanda=mystrdup(linie);

	if (comanda==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}

	comanda[strlen(comanda)-1]='\0';
	token=strtok(linie, " \n");

	// tokenul este acum pozitionat pe numarul cardului
	token=strtok(NULL, "\n");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		exit(-2);
	}
	aux=gaseste_LSC(x, card_number, nr_max);
	p=gaseste_card(aux, card_number);
	free(card_number);
	if (p==NULL)
	{
		printf("Nu exista cardul in baza de date(cancel)\n");
		free(comanda);
		exit(-2);
	}

	linie=myconcat("SUCCESS, ", comanda);
	if (linie==NULL)
	{
		free(comanda);
		exit(-2);
	}
	free(comanda);
	p=InserareCelHist(p, linie);
	if (p==NULL)
	{
		free(linie);
		exit(-2);
	}
}

//deblocheaza cardul conform cerintei
void unblock(TLL x, char *linie, int nr_max)
{
	TLL aux=NULL;
	TLC p=NULL;

	char *token=NULL, *card_number=NULL;
	token=strtok(linie, " \n");

	//acum tokenul este pozitionat pe numarul cardului
	token=strtok(NULL, "\n");
	card_number=mystrdup(token);
	if (!card_number)
	{
		exit(-2);
	}

	aux=gaseste_LSC(x, card_number, nr_max);
	p=gaseste_card(aux, card_number);
	free(card_number);
	if (p==NULL)
	{
		exit(-2);
	}
	strcpy(p->status, "ACTIVE");
	p->pin_tries=0;
}

//verifica un nou pin, daca este corect il actualizeaza pe cel vechi cu acesta
void pin_change(TLL x, char *linie, int nr_max, FILE *fp)
{
	TLL aux=NULL;
	TLC p=NULL;
	char *token=NULL, *comanda=NULL, *card_number=NULL, *new_pin=NULL;
	
	comanda=mystrdup(linie);
	if (comanda==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}
	comanda[strlen(comanda)-1]='\0';
	token=strtok(linie, " \n");

	//acum tokenul este pozitionat pe numarul cardului
	token=strtok(NULL, " ");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		exit(-2);
	}
	aux=gaseste_LSC(x, card_number, nr_max);
	p=gaseste_card(aux, card_number);
	free(card_number);
	if (p==NULL)
	{
		exit(-2);
	}

	//acum tokenul este pozitionat pe noul pin
	token=strtok(NULL, "\n");
	new_pin=mystrdup(token);
	if (!new_pin)
	{
		free(comanda);
		exit(-2);
	}

	//daca pinul nu respecta conditiile comanda pin_change esueaza
	if (verify_pin(new_pin)!=1)
	{
		fprintf(fp, "Invalid PIN\n");
		free(new_pin);
		linie=myconcat("FAIL, ", comanda);
		if (linie==NULL)
		{
			free(comanda);
			exit(-2);
		}

		free(comanda);
		p=InserareCelHist(p, linie);
		if (p==NULL)
		{
			free(linie);
			exit(-2);
		}
		return;
	}
	
	/*dupa toate verificarile daca totul se executa cu succes, se actualizeaza
	pinul, statusul cardului si faptul ca nu il mai are pe cel initial*/
	strcpy(p->pin, new_pin);
	free(new_pin);
	strcpy(p->status, "ACTIVE");
	linie=myconcat("SUCCESS, ", comanda);
	if (linie==NULL)
	{
		free(comanda);
		exit(-2);
	}

	free(comanda);
	p=InserareCelHist(p, linie);
	if (p==NULL)
	{
		free(linie);
		exit(-2);
	}
}

//afiseaza soldul curent
void balance_inquiry(TLL x, char *linie, int nr_max, FILE *fp)
{
	TLL aux=NULL;
	TLC p=NULL;
	char *token=NULL, *comanda=NULL, *card_number=NULL;
	
	comanda=mystrdup(linie);
	if (comanda==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}
	comanda[strlen(comanda)-1]='\0';
	
	token=strtok(linie, " \n");

	//acum tokenul este pozitionat pe numarul cardului
	token=strtok(NULL, "\n");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		exit(-2);
	}
	aux=gaseste_LSC(x, card_number, nr_max);
	p=gaseste_card(aux, card_number);
	free(card_number);
	if (p==NULL)
	{
		exit(-2);
	}

	fprintf(fp, "%d\n", p->balance);	
	linie=myconcat("SUCCESS, ", comanda);
	if (linie==NULL)
	{
		free(comanda);
		exit(-2);
	}

	free(comanda);
	p=InserareCelHist(p, linie);
	if (p==NULL)
	{
		exit(-2);
	}
}

//adauga suma pe cardul utilizatorului
void recharge(TLL x, char *linie, int nr_max, FILE *fp)
{
	TLL aux=NULL;
	TLC p=NULL;
	char *token=NULL, *comanda=NULL, *card_number=NULL, *sumi=NULL;
	int suma=0;
	
	comanda=mystrdup(linie);
	if (comanda==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}
	comanda[strlen(comanda)-1]='\0';
	
	token=strtok(linie, " \n");

	//acum tokenul este pozitionat pe numarul cardului
	token=strtok(NULL, " ");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		exit(-2);
	}
	aux=gaseste_LSC(x, card_number, nr_max);
	p=gaseste_card(aux, card_number);
	free(card_number);
	if (p==NULL)
	{
		exit(-2);
	}
	//acum tokenul este pozitionat pe suma
	token=strtok(NULL, "\n");
	sumi=mystrdup(token);
	if (!sumi)
	{
		free(comanda);
		exit(-2);
	}
	//scrie specific ipotezei in cazul in care suma nu se divide cu 10
	suma=multiple_of_ten(sumi, fp, RC);
	free(sumi);
	if (suma%10!=0)
	{
		linie=myconcat("FAIL, ", comanda);
		if (linie==NULL)
		{
			free(comanda);
			exit(-2);
		}
		free(comanda);
		p=InserareCelHist(p, linie);
		if (p==NULL)
		{
			free(linie);
			exit(-2);
		}
		return;
	}

	p->balance=p->balance+suma;
	fprintf(fp, "%d\n", p->balance);
	linie=myconcat("SUCCESS, ", comanda);
	if (linie==NULL)
	{
		free(comanda);
		exit(-2);
	}

	free(comanda);
	p=InserareCelHist(p, linie);
	if (p==NULL)
	{
		free(linie);
		exit(-2);
	}
}

//extrage suma de bani de pe card
void cash_withdrawal(TLL x, char *linie, int nr_max, FILE *fp)
{
	TLL aux=NULL;
	TLC p=NULL;
	char *token=NULL, *comanda=NULL, *card_number=NULL, *sumi=NULL;
	int suma=0;
	
	comanda=mystrdup(linie);
	if (comanda==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}
	comanda[strlen(comanda)-1]='\0';
	
	token=strtok(linie, " \n");

	//acum tokenul este pozitionat pe numarul cardului
	token=strtok(NULL, " ");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		exit(-2);
	}
	aux=gaseste_LSC(x, card_number, nr_max);
	p=gaseste_card(aux, card_number);
	free(card_number);
	if (!p)
	{
		free(comanda);
		exit(-2);
	}
	//acum tokenul este pozitionat pe suma
	token=strtok(NULL, "\n");
	sumi=mystrdup(token);
	if (!sumi)
	{
		free(comanda);
		exit(-2);
	}
	suma=multiple_of_ten(sumi, fp, CS);
	free(sumi);
	if ((suma%10!=0)||(p->balance-suma<0))
	{
		if((p->balance-suma<0)&&(suma%10==0))
		{
			fprintf(fp, "Insufficient funds\n");
		}

		linie=myconcat("FAIL, ", comanda);
		if (linie==NULL)
		{
			free(comanda);
			exit(-2);
		}
		free(comanda);
		p=InserareCelHist(p, linie);
		if (p==NULL)
		{
			free(linie);
			exit(-2);
		}
		return;
	}

	p->balance=p->balance-suma;
	fprintf(fp, "%d\n", p->balance);
	linie=myconcat("SUCCESS, ", comanda);
	if (linie==NULL)
	{
		free(comanda);
		exit(-2);
	}
	free(comanda);
	p=InserareCelHist(p, linie);
	if (p==NULL)
	{
		free(linie);
		exit(-2);
	}
}

void transfer_funds(TLL x, char *linie, int nr_max, FILE *fp)
{
	TLL aux=NULL;
	TLC s=NULL, d=NULL;
	char *token=NULL, *comanda=NULL, *card_number=NULL;
	char *sumi=NULL, *comanda2=NULL, *linie2=NULL;
	int suma=0;
	
	comanda=mystrdup(linie);
	if (comanda==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}
	comanda[strlen(comanda)-1]='\0';
	comanda2=mystrdup(linie);
	if (comanda2==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}
	comanda2[strlen(comanda2)-1]='\0';

	token=strtok(linie, " \n");

	//acum tokenul este pozitionat pe numarul cardului sursa
	token=strtok(NULL, " ");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		free(comanda2);
		exit(-2);
	}
	aux=gaseste_LSC(x, card_number, nr_max);
	s=gaseste_card(aux, card_number);
	free(card_number);
	if (!s)
	{
		free(comanda);
		free(comanda2);
		exit(-2);
	}

	//acum tokenul este pozitionat pe numarul cardului destinatie
	token=strtok(NULL, " ");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		free(comanda2);
		exit(-2);
	}
	aux=gaseste_LSC(x, card_number, nr_max);
	d=gaseste_card(aux, card_number);
	free(card_number);
	if (!d)
	{
		free(comanda);
		free(comanda2);		
		exit(-2);
	}

	//acum tokenul este pozitionat pe suma
	token=strtok(NULL, "\n");
	sumi=mystrdup(token);
	if (!sumi)
	{
		free(comanda);
		free(comanda2);
		exit(-2);
	}
	suma=multiple_of_ten(sumi, fp, T);
	free(sumi);
	if ((suma%10!=0)||(s->balance-suma<0))
	{
		free(comanda2);
		if((s->balance-suma<0)&&(suma%10==0))
		{
			fprintf(fp, "Insufficient funds\n");
		}

		linie=myconcat("FAIL, ", comanda);
		if (linie==NULL)
		{
			free(comanda);
			exit(-2);
		}
		free(comanda);
		s=InserareCelHist(s, linie);
		if (s==NULL)
		{
			free(linie);
			exit(-2);
		}
		return;
	}

	s->balance=s->balance-suma;
	d->balance=d->balance+suma;
	fprintf(fp, "%d\n", s->balance);	
	linie2=myconcat("SUCCESS, ", comanda2);
	if (linie2==NULL)
	{
		free(comanda);
		free(comanda2);
		exit(-2);
	}
	free(comanda2);
	d=InserareCelHist(d, linie2);
	if (d==NULL)
	{
		free(comanda);
		free(linie2);
		exit(-2);
	}
	linie=myconcat("SUCCESS, ", comanda);
	if (linie==NULL)
	{
		free(comanda);
		exit(-2);
	}
	free(comanda);
	s=InserareCelHist(s, linie);
	if (s==NULL)
	{
		free(linie);
		exit(-2);
	}
}

void reverse_transaction(TLL x, char *linie, int nr_max, FILE *fp)
{
	TLL aux=NULL;
	TLC s=NULL, d=NULL; //s va fi cardul sursa, iar d destinatie
	char *token=NULL, *comanda=NULL, *card_number=NULL;
	char *sumi=NULL, *source=NULL, *sourc_dest=NULL;
	char *need_space=NULL, *everything=NULL;
	int suma=0;
	
	comanda=mystrdup(linie);
	if (comanda==NULL)
	{
		printf("eroare la alocarea stringului\n");
		exit(-2);
	}
	comanda[strlen(comanda)-1]='\0';
	
	token=strtok(linie, " \n");

	//acum tokenul este pozitionat pe numarul cardului sursa
	token=strtok(NULL, " ");
	card_number=mystrdup(token);
	if (!card_number)
	{
		exit(-2);
	}

	aux=gaseste_LSC(x, card_number, nr_max);
	s=gaseste_card(aux, card_number);
	if (!s)
	{
		free(comanda);
		free(card_number);
		exit(-2);
	}
	source=myconcat(card_number, " "); // SURSA_
	free(card_number);
	if (!source)
	{
		free(comanda);
		exit(-2);
	}
	//acum tokenul este pozitionat pe numarul cardului destinatie
	token=strtok(NULL, " ");
	card_number=mystrdup(token);
	if (!card_number)
	{
		free(comanda);
		free(source);
		exit(-2);
	}
	
	aux=gaseste_LSC(x, card_number, nr_max);
	d=gaseste_card(aux, card_number);
	if (!d)
	{
		free(card_number);
		free(comanda);
		free(source);
		exit(-2);	
	}
	sourc_dest=myconcat(source, card_number); // SURSA_DESTINATIE
	free(card_number);
	free(source);
	if (!sourc_dest)
	{
		free(comanda);
		exit(-2);	
	}
	//acum tokenul este pozitionat pe suma
	token=strtok(NULL, "\n");
	sumi=mystrdup(token);
	if (!sumi)
	{
		free(comanda);
		free(sourc_dest);
		exit(-2);
	}
	suma=multiple_of_ten(sumi, fp, RV);
	need_space=myconcat(sourc_dest, " ");
	free(sourc_dest);
	if (!need_space)
	{
		free(comanda);
		free(sumi);
		exit(-2);
	}
	everything=myconcat(need_space, sumi);
	free(sumi);
	free(need_space);
	if (!everything)
	{
		free(comanda);
		exit(-2);
	}
	if (d->balance-suma>=0)
	{
		s->balance=s->balance+suma;
		d->balance=d->balance-suma;
		d= DistrHistRev(d, everything);
		linie=myconcat("SUCCESS, ", comanda);
		if (linie==NULL)
		{
			exit(-2);
		}
		s=InserareCelHist(s, linie);
	}
	else
		{
			fprintf(fp, "The transaction cannot be reversed\n");
		}
	free(comanda);
	free(everything);
}

int main ()
{ 
  char buf[N], comenzi[N][M], OK=1;
  char *linie=NULL; 
  TLL x = NULL;
  int nr_linii_comenzi=0, i=0, nr_max_carduri=0, nr_curent_carduri=0, com=0;
  int au_existat_carduri=0;

  FILE *input_file = fopen("input.in", "r");

  if (input_file == NULL) 
  {
	fprintf(stderr, "File not found\n");
	return -2;
  }

  // Copiaza in vectorul "comenzi" linie cu linie inputul
  while (fgets(buf, 200, input_file))
  {
    strcpy(comenzi[nr_linii_comenzi], buf);
    nr_linii_comenzi++;
  }
  fclose(input_file);

  FILE *output_file = fopen("output.out", "w");
  if (output_file == NULL)
  {
	fprintf(stderr, "Could not open or create file \n");
	return -2;
  }

  x=InitLC(OK);
  if (x==NULL)
  {
  	return(-2);
  }

  //extragem nr_max_carduri
  nr_max_carduri=atoi(comenzi[0]);
  
  /*sparge fiecare linie din input in comanda coresp. pe care o executa
  cand se termina comenzile se incheie practic prelucrarea inputului*/  
  for (i = 1; i < nr_linii_comenzi; ++i)
  {	
  	linie = comenzi[i];
	com = cmd_to_int(comenzi[i][0], comenzi[i][2]);
	
	//fiecare comanda trece printr-un switch si intra pe propriul caz
	switch(com)
	{
		case A:
      		{
      		x= add_card(x, linie, &nr_max_carduri,
      	 			&nr_curent_carduri, output_file);
         	break;
         	}
		case D:
      		{
      		x= del_card(x, linie, nr_max_carduri,
      	 				&nr_curent_carduri, &au_existat_carduri);
        	break;
        	}
      	case I:
      		{
      		insert(x, linie, nr_max_carduri ,output_file);
      		break;
      		}
      	case CN: 
      		{
      		cancel(x, linie, nr_max_carduri);
      		break;
   			}
  		case U:
    		{
    		unblock(x, linie, nr_max_carduri);
      		break;
      		}
      	case P:
    		{
    		pin_change(x, linie, nr_max_carduri, output_file);
      		break;
      		}
      	case B:
      		{
      		balance_inquiry(x, linie, nr_max_carduri, output_file);
      		break;
      		}
      	case RC:
      		{
      		recharge(x, linie, nr_max_carduri, output_file);
      		break;
      		}
      	case CS:
      		{
      		cash_withdrawal(x, linie, nr_max_carduri, output_file);
      		break;
      		}
      	case T:
      		{
      		transfer_funds(x, linie, nr_max_carduri, output_file);
      		break;
      		}
      	case RV:
      		{
      		reverse_transaction(x, linie, nr_max_carduri, output_file);
      		break;
      		}
		case S:
      		{
      		show(x, linie, nr_max_carduri, au_existat_carduri, output_file);
        	break;
        	} 	   
    }   		
  }
  	linie=NULL;
	fclose(output_file);
	DistrToateLSC(&x);
	return 0;
}