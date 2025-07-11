package ah.poiar.data.event;

import ah.poiar.data.generic.PoIarPlayer;

public final class CTransactionEvent {

    public final PoIarPlayer player;
    public final long time, delay;

    public CTransactionEvent(PoIarPlayer player) {
        this.time = System.currentTimeMillis();
        this.player = player;
        this.delay = player.getTransactionData().transactionPing;
    }
}
