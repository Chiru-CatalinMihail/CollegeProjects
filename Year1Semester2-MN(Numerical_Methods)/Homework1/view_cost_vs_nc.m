function view_cost_vs_nc(file_points)
	%ca la task-ul 1 face citirea asigurandu-se ca file_points este de tip ascii
  points = load("-ascii", file_points);
  cost=[];
  NC=[1:10];
  %ia primi 10 centroizi si le face costul
  for i= NC
    centroids=clustering_pc(points, i);
    cost(i)= compute_cost_pc(points, centroids);
  endfor
  
  %face graficul cost dependenta de centroizi
  plot(NC, cost);
  xlabel("Cluster count");
  ylabel("Cost");
  end

