% computes the NC centroids corresponding to the given points using K-Means
function centroids = clustering_pc(points, NC)
	
  %initializam variabilele pe care le vom utiliza
  centroids = [];
  centr_urm=zeros(NC, 3);
  suma_ox=0;
  suma_oy=0;
  suma_oz=0;
  proximity=[];
  nr_puncte=0;
  dist_min=0;
  dist=0;
  
 
  %nr_puncte arata cate puncte exista
  nr_puncte=size(points)(1);
  %rand_pts randomizeaza unic intr-un vector nr. de la 1 la cate puncte avem
  rand_pts= randperm(nr_puncte);
  
  %primii centroizi sunt primele NC puncte indicate de randomizare
  centroids= points(rand_pts(1:NC), :);
  
  %ciclam pana indeplinim conditia ca la urmatoarea iteratie sa avem aceeasi
  %centroizi
  while(1)
    %la fiecare ciclare face dist_min cu infinit ca sa putem opera cu ea din nou
    dist_min=inf;
    %elibereaza valorile anterioare din proximity
    proximity=0;
    for i=1:nr_puncte
      %initializeaza dist_min cu distanta pana la primul centroid
      dist_min=norm(centroids(1, 1:3)-points(i, 1:3));
      
      %proximity arata de care centroid este un pct cel mai apropiat
      %initial este apropiat de primul punct intalnit
      proximity(i) = 1;
      
      %updateaza dist_min si proximitatea daca exista distante mai mici fata de
      %alti centroizi
      for j=2:NC
        dist=norm(centroids(j, 1:3)-points(i, 1:3));
        if(dist<dist_min)
          dist_min=dist;
          proximity(i)=j;
        endif
      endfor
    endfor
   
   
   %calculeaza noii centroizi ca media aritmetica pe fiecare coordonata a
   %punctelor din proximitate 
   for i=1:NC
     puncte_per_centro=sum(proximity==i);
     %initializeaza pentru fiecare parcurgere variabilele corespunzatoare
     %fiecarei axe
     suma_ox=0;
     suma_oy=0;
     suma_oz=0;
     for j= 1:nr_puncte
          %daca punctul se afla in proximitatea centroidului aduna la fiecare
          %axa coordonatele punctului
          if(proximity(j)==i)
          suma_ox=suma_ox+points(j, 1);
          suma_oy=suma_oy+points(j, 2);
          suma_oz=suma_oz+points(j, 3);
          endif
     endfor
     sumix=[suma_ox suma_oy suma_oz];
     centr_urm(i, 1:3) = sumix/ puncte_per_centro;
   endfor
 %conditia de iesire din while
  if(isequal(centroids, centr_urm))
    break;
  endif
  %daca nu s-a iesit itereaza spre pasul urmator
  centroids=centr_urm; 
  endwhile
end
