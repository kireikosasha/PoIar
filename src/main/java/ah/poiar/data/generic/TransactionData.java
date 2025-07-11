package ah.poiar.data.generic;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionData {
    public boolean transactionSentKeep = false;
    public boolean transactionBoot = true;
    public long transactionTime = 0, transactionLastTime = 0, transactionPing = 0;
    public int transactionId = 0;
}
