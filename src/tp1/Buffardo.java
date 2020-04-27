package tp1;

import java.util.LinkedList;
import java.util.HashMap;

public class Buffardo {


	private LinkedList<Article>    lugares;
	private HashMap<String,String> consumerState;
	private Article                article;
	private final int              bufferSize;
	


	public Buffardo(int i,int threadQuantity) {
		
		bufferSize          = i;
		lugares             = new LinkedList<Article>();
		consumerState       = new HashMap<String,String>();
		article             = new Article();
		
		for(int j=0; i<threadQuantity; j++) {
			consumerState.put(("Consumidor "+j), Estados.DISPONIBLE.name());
		}
       
	}


	public synchronized boolean addItem(Article art) {
		if(lugares.size()<bufferSize) {
			lugares.add(art);
			System.out.printf("Articulo a�adido por %s\n", Thread.currentThread().getName());
			notify();
			return true;
		}
		else {
			notify();
			return false;
		}
	}

	public synchronized void takeItem(Consumidor consumidor) {
		while(lugares.size()==0) {
			try {
				consumidor.setEstado(Estados.DISPONIBLE);
				setConsumerState(Thread.currentThread().getName(),consumidor.getEstado());
				wait();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(article.getArtConsum()<1000) {
			lugares.remove();
			article.incrementArtConsum();
			consumidor.setEstado(Estados.OCUPADO_CONSUMIENDO);
			setConsumerState(Thread.currentThread().getName(),consumidor.getEstado());
			System.out.printf("Articulo consumido por %s\n", Thread.currentThread().getName());
			notify();
		}
			
	}
	
	public int get_Counter() {
		return lugares.size();
	}
	
	public void setConsumerState(String id, Estados state) {
			consumerState.put(id, state+"");
	}
	
	public HashMap<String,String> getConsumerState(){
		return consumerState;
	}
}