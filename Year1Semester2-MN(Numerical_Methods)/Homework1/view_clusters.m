% outputs a graphical representation of the clustering solution
function view_clusters(points, centroids)
	% TODO graphical representation coded here 
  nr_puncte=size(points)(1);
  %arata nr de centroizi
  NC=size(centroids)(1);
  dist_min=inf;
  for i=1:nr_puncte
    %similar taskului 2 distanta minima este initial distanta
    %pana la primul centroid
    dist_min=norm(centroids(1, 1:3)-points(i, 1:3));
    %proximity indica spre primul centroid gasit
     proximity(i) = 1;   
    for j=2:NC
        dist=norm(centroids(j, 1:3)-points(i, 1:3));
        %updateaza distanta si centroidul cel mai apropiat daca se gaseste o
        %distana mai mica intre punct si unul din cei NC centroizi
        if(dist<dist_min)
          dist_min=dist;
          proximity(i)=j;
        endif
    endfor
  endfor
  %afiseaza grafic punctele de dimensiune 12, cu marginea conturata, umplute si colorate
  scatter3(points(:, 1), points(:, 2), points(:, 3), [], proximity(:), 12, 'filled', 'MarkerEdgeColor', 'k');   
end

