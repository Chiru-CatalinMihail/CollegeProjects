//Chiru Catalin-Mihail 312CB
/*-- structuri.h --- Structurile folosite in rezolvarea temei ---*/
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

typedef struct lsc
{
	int index;
	struct card *card;
	struct lsc * urm;
} TCL, *TLL, **ALL; /* tipurile Celula lsc, 
							Lista lsc si Adresa_Lista lsc */

typedef struct card
{
	char* card_number;
	char* pin;
	int pin_tries;
	char* exp_date; //expiry date
	char* cvv;
	int balance;
	char* status; //status
	struct istoric *history;
	struct card * urm;
} TCC, *TLC, **ALC; /* tipurile Celula card, Lista card si Adresa_Lista card */

typedef struct istoric
{
	char* info_h;
	struct istoric * urm;
} TCH, *TLH, **ALH; /* analog cu tipurile de la lsc, dar pentru istoric */
