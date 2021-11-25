# Nejaké poznámky k DB a API

![LocalDB drawio](https://user-images.githubusercontent.com/49959692/142734649-b14856cb-3b8f-4bb3-b8f5-1f5cfb0950c6.png)

## Account

Zatiaľ je dohoda taká, že bude iba 1 account. Teda v tejto tabuľke máme iba 1 riadok a z neho sa potom odvádzajú ďalšie veci..

[Account API ref.](https://developers.stellar.org/api/resources/accounts/object/) 

    - (PK) accountId - public key accountu
    - firstName
    - lastName 
    - pin
    - privateKey
    - sequence - "This account’s current sequence number. For use when submitting this account’s next transaction."

## Contact

Kontakty riešime asi iba lokálne, v api som nikde nevidela nejaké kontakty. V accounte majú field "data" ale veľmi som nepochopila čo sa s tým dá spraviť ("data object - An array of account data fields.")

    - (PK) contactId - public key kontaktu
    - (FK) sourceAccount - public key accountu ktorému patrí kontakt
    - name

## Payment

Z toho čo som pochopila, tak pre fellas zo stellaru je Payment = Operation a Transaction je nejaký wrapper kam sa pridávajú rôzne operácie. Zatiaľ v lokálnej db riešim iba tú operation časť lebo o transakciách asi nepotrebujeme ukladať až tak veľa informácií..

[Operation API ref.](https://developers.stellar.org/api/resources/operations/object/)
    
[Payment API ref.](https://developers.stellar.org/api/resources/operations/object/payment/)

[Transaction API ref.](https://developers.stellar.org/api/resources/transactions/object/)

    - (PK) paymentId
    - (FK) sourceAccount - public key accountu ktorý odoslal platbu
    - transactionHash
    - transactionSuccessful
    - createdAt
    - assetType - môžu byť 4 druhy:
        - native = lumeny - default mena stellaru
        - credit_alphanum4 = max 4-znakový asset_code
        - credit_alphanum12 = max 12-znakový asset_code
        - liquidity_pool_shares = nemám šajnu, asi môžeme ignorovať :D
    - assetCode - **kuk dole
    - assetIssuer - **kuk dole
    - from - public key odosielateľa (a.k.a. to isté ako v sourceAccount... ale api by mala vracať aj to aj to tak nech sú FK jednotné ako sourceAccount)
    - to - public key prijímateľa
    - amount

\*\* Tuto sa to kinda komplikuje, lebo:

- ak je asset_type = 'native', tak pri buildovaní transakcie netreba špecifikovať asset_code ani asset_issuer. Ani api to pre native platby cez get nevracia. Ale lokálne by som do db aj tak ukladala asset_code = "native" a asset_issuer = náš public key aby sa to dalo potom použiť pre balances

- ak je asset_type niečo iné tak treba špecifikovať asset_code aj asset_issuer. 
    - asset_code by mal byť názov meny (eur, usd, magické fazulky, ...) 
    - asset_issuer asi môžeme použiť náš public key. Ak to teda správne chápem, vieme vytvárať vlastné typy assetov a konkrétny asset je identifikovaný s asset_code + asset_issuer.  
    
## Balances

[Balances API ref.](https://developers.stellar.org/api/resources/accounts/object/) - balances sú v rámci accountu

[Asset API ref.](https://developers.stellar.org/api/resources/assets/object/)

Account by mal obsahovať zvlášť "balance" pre každý asset a.k.a každú kombináciu asset_code + asset_issuer. Teda máme 1 balance pre natívne platby, 1 na usd atď. Teda basically jeden view v tom myBalance patrí jednej mene.

    - (PK part1) assetCode
    - (PK part2) assetIssuer
    - (FK) sourceAccount
    - assetType
    - balance
    - limit

## BalanceHistory (?)

Toto je nateraz optional ale ak by sme chceli vykreslovať fancy graf s pohybmi na účte, sem by sme si mali ukladať priebežné balancy. Toto by sme mali iba lokálne.

    - (PK) balanceId
    - (FK) assetCode
    - (FK) assetIssuer
    - balance

\+ bolo by potom cool odniekiaľ vytiahnuť aj nejaký timestamp kedy došlo k zmene..  v tom balance objekte som nič také nevidela
