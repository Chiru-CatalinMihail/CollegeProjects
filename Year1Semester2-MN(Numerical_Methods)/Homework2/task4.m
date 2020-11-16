function [A_k S] = task4(image, k)
  
  first_k_cols=[1:k];
  
  %obtinem matricea corespunzatoare imaginii si dimensiunile sale
  A=double(imread(image));
  [linii coloane]=size (A);
  n=coloane;
  
  %obtinem vectorul miu// pasii 1-2 de la cerinta 3
  for i=1:linii
      linia_crt=A(i,:);
      miu(i)=sum(linia_crt)/n;
      A(i,:)=A(i,:)-miu(i);    
  endfor
  
  %construim matricea de covarianta
  Z=A*(transpose(A)/(n-1));
  miu=transpose(miu);
  
  %valorile si vectorii proprii ai lui Z
  [V S]=eig(Z);
  
  %Similar pasilor 6-7 de la cerinta 3
  W=V(:,first_k_cols); % W este dat de primele k coloane din V
  Y=transpose(W)*A;
  A_k= W*Y+ miu;
  
endfunction