% computes a clustering solution total cost
function cost = compute_cost_pc(points, centroids)
	cost = 0; 
	% TODO compute clustering solution cost
  nr_puncte=size(points)(1);
  NC=size(centroids)(1);
  dist_min=[];
  dist=inf;
  %itereaza prin toate punctele si stabileste pentru fiecare punct
  %distanta cea mai mica dintre el si centroidul sau corespunzator
  for i=1:nr_puncte
    dist_min(i)=norm(centroids(1, 1:3)-points(i, 1:3));
    for j=2:NC
        dist=norm(centroids(j, 1:3)-points(i, 1:3));
        if(dist<dist_min(i))
          dist_min(i)=dist;
        endif
    endfor
  endfor
  %costul este suma tuturor distantelor minime
  cost=sum(dist_min);

end

