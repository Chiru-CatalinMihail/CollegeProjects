function task2(image, cerinta)
 
    A=double(imread(image));
    [m n]=size(A);
    [u, s, v] = svd(A);
    
    if(cerinta==5)
       [A s]=task3(image, min(m,n));
    endif
    
    %transforma pe s in vector 
    s=diag(s);
    suma_numitor=sum(s);
 
    %graficul 1
    figure(1);
    graph1=plot(s);
    set(graph1,'LineWidth',2);
    
    for i=1:min(m,n)
      %obtinem cate o comprimare pentru fiecare k
      k(i)=i;
      A_k = task1(image, i);
      [u, s, v] = svd(A_k);
      s=diag(s);
      
      %informatia data de primele k valori singulare//necesara pentru graficul 2
      info(i)=sum(s)/suma_numitor;
      if(cerinta==5)
         [A_k s]=task3(image, i);
         info(i)=sum(diag(s(1:k(i),1:k(i))))/sum(diag(s));
      endif
      %eroarea aproximarii pentru matricea A//necesara pentru graficul 3
      eroare(i)=sum(sum(((A-A_k).^2)/(m*n)));
    endfor
    
    %graficul 2
    figure(2);
    graph2=plot(info);
    set(graph2,'LineWidth',2);
    
    %graficul 3
    figure(3);
    graph3=plot(eroare);
    set(graph3,'LineWidth',2);    
    
    compression_rate=(m*k+n*k+k)/(m*n);
    if(cerinta==5)
       compression_rate=(2*k+1)/n;
    endif
    
    %graficul 4
    figure(4);
    graph4=plot(compression_rate);
    set(graph4,'LineWidth',2);    
    
end