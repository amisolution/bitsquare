/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bisq.gui.util.validation.params;

import org.bitcoinj.core.*;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.utils.MonetaryFormat;

public class PivxParams extends NetworkParameters {

    private static PivxParams instance;

    public static synchronized PivxParams get() {
        if (instance == null) {
            instance = new PivxParams();
        }
        return instance;
    }

    // We only use the properties needed for address validation
    public PivxParams() {
        super();
        addressHeader = 30;
        p2shHeader = 13;
        acceptableAddressCodes = new int[]{addressHeader, p2shHeader};
    }

    // default dummy implementations, not used...
    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }

    @Override
    public void checkDifficultyTransitions(StoredBlock storedPrev, Block next, BlockStore blockStore) throws VerificationException, BlockStoreException {
    }

    @Override
    public Coin getMaxMoney() {
        return null;
    }

    @Override
    public Coin getMinNonDustOutput() {
        return null;
    }

    @Override
    public MonetaryFormat getMonetaryFormat() {
        return null;
    }

    @Override
    public String getUriScheme() {
        return null;
    }

    @Override
    public boolean hasMaxMoney() {
        return false;
    }
}