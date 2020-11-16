function A_k = task1(image, k)
  
  %obtinem matricea corespunzatoare imaginii si dimensiunile sale
  A_k=double(imread(image));
  [linii coloane]=size (A_k);
  
  %ne construim vectorii de unde dorim sa pastram valorile din u s si v
  pastreaza=[1:k];
  
  %descompunem valorile singulare
  [u, s, v] = svd(A_k);
  
  %actualizam cu ajutorul lui k valorile din u s si v si reconstruim matricea A
  u=u(:,pastreaza);
  s=s(pastreaza,:);
  s=s(:,pastreaza);
  
  %avem nevoie de reducerea liniilor transpusei lui v
  v=transpose(v);
  v=v(pastreaza,:);
  
  A_k=u*s*v;

 end