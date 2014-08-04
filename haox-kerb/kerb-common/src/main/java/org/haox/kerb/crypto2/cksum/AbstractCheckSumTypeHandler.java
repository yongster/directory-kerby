package org.haox.kerb.crypto2.cksum;

import org.haox.kerb.crypto2.AbstractCryptoTypeHandler;
import org.haox.kerb.crypto2.CheckSumTypeHandler;
import org.haox.kerb.crypto2.enc.EncryptProvider;
import org.haox.kerb.spec.KrbException;

public abstract class AbstractCheckSumTypeHandler
        extends AbstractCryptoTypeHandler implements CheckSumTypeHandler {

    public AbstractCheckSumTypeHandler(EncryptProvider encProvider,
                                         HashProvider hashProvider) {
        super(encProvider, hashProvider);
    }

    @Override
    public String name() {
        return cksumType().getName();
    }

    @Override
    public String displayName() {
        return cksumType().getDisplayName();
    }

    public byte[] calculateChecksum(byte[] data) throws KrbException {
        throw new UnsupportedOperationException();
    }

    public boolean verifyChecksum(byte[] data, byte[] checksum) throws KrbException {
        throw new UnsupportedOperationException();
    }

    public byte[] calculateKeyedChecksum(byte[] data,
                                         byte[] key, int usage) throws KrbException {
        throw new UnsupportedOperationException();
    }

    public boolean verifyKeyedChecksum(byte[] data,
                                       byte[] key, int usage, byte[] checksum) throws KrbException {
        throw new UnsupportedOperationException();
    }
}
