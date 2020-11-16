function [A_k S] = task3(image, k)
  
  first_k_cols=[1:k];
  
  %obtinem matricea corespunzatoare imaginii si dimensiunile sale
  A=double(imread(image));
  [linii coloane]=size (A);
  n=coloane;
  
  for i=1:linii
    
      linia_crt=A(i,:);
      
      %Pasul 1
      miu(i)=sum(linia_crt)/n;
      
      %Pasul 2
      A(i,:)=A(i,:)-miu(i);    

  endfor
  
  %Pasul 3
  Z=transpose(A)/sqrt(n-1);
  miu=transpose(miu);
 
  %Pasul 4 // descompunem valorile pentru Z
  [U, S, V] = svd(Z);
  
  %Pasul 5
  W=V(:,first_k_cols);
  
  %Pasul 6
  Y=transpose(W)*A;
  
  %Pasul 7
  A_k= W*Y + miu;
  
endfunction