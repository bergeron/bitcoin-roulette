package main.java.pw.bitcoinroulette.server.bitcoin;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com._37coins.bcJsonRpc.pojo.Block;
import com._37coins.bcJsonRpc.pojo.Transaction;

public class NewBlockListener implements Observer {
	
	private SessionFactory sessionFactory;
	
	public NewBlockListener(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void update(Observable o, Object arg) {
		
		System.out.println("new block");
		Block b = (Block)arg;
		List<String> txIds = b.getTx();
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List txns = session.createCriteria(Transaction.class).add(Restrictions.gt("confirmations", 6)).list();
		session.getTransaction().commit();
		session.close();
		
	}
}
