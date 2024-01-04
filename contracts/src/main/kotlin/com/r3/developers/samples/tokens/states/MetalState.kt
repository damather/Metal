package com.r3.developers.samples.tokens.states

import com.r3.developers.samples.tokens.contracts.MetalExchangeContract
import net.corda.v5.application.crypto.DigestService
import net.corda.v5.crypto.SecureHash
import net.corda.v5.ledger.utxo.BelongsToContract
import net.corda.v5.ledger.utxo.ContractState
import net.corda.v5.ledger.utxo.observer.UtxoLedgerTokenStateObserver
import net.corda.v5.ledger.utxo.observer.UtxoToken
import net.corda.v5.ledger.utxo.observer.UtxoTokenFilterFields
import net.corda.v5.ledger.utxo.observer.UtxoTokenPoolKey
import java.math.BigDecimal
import java.security.PublicKey

@BelongsToContract(MetalExchangeContract::class)
data class MetalExchangeState(
    val issuer: SecureHash,
    val owner: SecureHash,
    val symbol: String,
    val amount: BigDecimal,
    private val participants: List<PublicKey>) : ContractState {
    override fun getParticipants(): List<PublicKey> {
        return  participants
    }

}

/*
By implementing the UtxoLedgerTokenStateObserver, this observer will generate fungible states/tokens for
each produced MetalExchange state when persisting a finalized transaction to the vault.
 */
class MetalExchangeStateObserver: UtxoLedgerTokenStateObserver<MetalExchangeState>{
    override fun getStateType(): Class<MetalExchangeState> {
        return MetalExchangeState::class.java
    }

    override fun onCommit(state: MetalExchangeState, digestService: DigestService): UtxoToken {
        //generate a pool with key - type, issuer and symbol to mint the tokens
        val poolKey = UtxoTokenPoolKey(MetalExchangeState::class.java.getName(), state.issuer, state.symbol)
        return UtxoToken(poolKey, state.amount, UtxoTokenFilterFields(null, state.owner))
    }

}