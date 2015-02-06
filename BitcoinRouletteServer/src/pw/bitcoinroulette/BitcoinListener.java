package pw.bitcoinroulette;


import java.util.HashMap;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.azazar.bitcoin.jsonrpcclient.BitcoinPaymentListener;

/**
 * @author Mikhail Yevchenko
 */
public class BitcoinListener implements Runnable {
    

    public final BitcoinJSONRPCClient bitcoin;
    private String lastBlock, monitorBlock = null;
    int monitorDepth;
    private static HashMap<String, BitcoinPaymentListener> addressToListener = new HashMap<String, BitcoinPaymentListener>();

    public BitcoinListener(BitcoinJSONRPCClient bitcoin) {
    	this.bitcoin = bitcoin;
        this.lastBlock = null;
        this.monitorDepth = 6;
    }

    /**
     * @param address	the address to listen for
     * @param listener	called when address receives a payment
     */
    public synchronized void addListener(String address, BitcoinPaymentListener listener) {
        addressToListener.put(address, listener);
    }

    private void updateMonitorBlock() throws BitcoinException {
        monitorBlock = lastBlock;
        for(int i = 0; i < monitorDepth && monitorBlock != null; i++) {
            Bitcoin.Block b = bitcoin.getBlock(monitorBlock);
            monitorBlock = b == null ? null : b.previousHash();
        }
    }

    public synchronized void checkPayments() throws BitcoinException {
        Bitcoin.TransactionsSinceBlock t = monitorBlock == null ? bitcoin.listSinceBlock() : bitcoin.listSinceBlock(monitorBlock);
        for (Bitcoin.Transaction transaction : t.transactions()){
            if ("receive".equals(transaction.category())) {
            	BitcoinPaymentListener listener = addressToListener.get(transaction.address());
            	if(listener != null)
            		listener.transaction(transaction);
            }
        }
        
        if (!t.lastBlock().equals(lastBlock)) {
            lastBlock = t.lastBlock();
            updateMonitorBlock();
        }
    }

    private boolean stop = false;
    
    public void stopAccepting() {
        stop = true;
    }
    
    private long checkInterval = 5000;

    /**
     * Get the value of checkInterval
     *
     * @return the value of checkInterval
     */
    public long getCheckInterval() {
        return checkInterval;
    }

    /**
     * Set the value of checkInterval
     *
     * @param checkInterval new value of checkInterval
     */
    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public void run() {
        stop = false;
        long nextCheck = 0;
        while(!(Thread.interrupted() || stop)) {
            if (nextCheck <= System.currentTimeMillis())
                try {
                    nextCheck = System.currentTimeMillis() + checkInterval;
                    checkPayments();
                } catch (BitcoinException ex) {
                	ex.printStackTrace();
                }
            else
                try {
                    Thread.sleep(Math.max(nextCheck - System.currentTimeMillis(), 100));
                } catch (InterruptedException ex) {
                	ex.printStackTrace();
                }
        }
    }
}
