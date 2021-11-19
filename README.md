# MOBV-project
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome)  
## Zadanie

Vytvorte Android aplikáciu v jazyku Kotlin, v ktorej si budeme môcť vygenerovať krypto penaženku pre Steller Lumeny a s možnosťou ich posielania a zobrazovania transakcií a zostatku. Semestralne zadanie budete vypracovavat vo štvorici. Odovzdať ho je potrebné pred 11. cvičením. Na 11. a 12. cvičení ho budete prezentovat.

```diff 
! DEADLINE: 2.12. 6:00
```

## Hodnotenie

* Vygenerovanie a naplnenie účtu na testnete Stellar (Horizon). <b>3 body</b>
* Pridanie existujúceho účtu na testnete Stellar (Horizon). <b>3 body</b>
* Odoslanie transakcie na testnete Stellar (Horizon). <b>5 bodov</b>
* Načítanie a uloženie aktuálneho zostatku na účte do lokálnej databázy. <b>3 body</b>
* Načítanie a synchronizácia transakcií (kreditových, debetných) do lokálnej databázy. <b>10 bodov</b>
* Zobrazenie transakcií a zostatku z lokálnej databázy. <b>5 bodov</b>
* Uloženie do lokálnej databázy kontakty na prijímateľov. <b>5 bodov</b>
* Zobrazenie uložených kontaktov, možnosť výberu prijímateľa pri odosielaní transakcie. <b>10 bodov</b>
* Používateľské rozhranie na úrovni s intuitívnym ovládaním. <b>10 bodov</b>

```diff 
! Spolu max 50 Bodov ( min. 25 bodov na zápočet )
```

Pri celom zadaní je nutné použiť správny návrh kódu a použitie architektúry View - Model - ViewModel, LiveData, Databinding, Repozitár, Room Databázu a RecyclerView.

Pozn.: Práca na pozadí sa vykonáva v Repozitári a volá sa vo ViewModel, výsledky sú v LiveData vo ViewModel.

## Krypto peňaženka cez [Stellar SDK](https://www.stellar.org/)
* https://developers.stellar.org/docs/tutorials/create-account/
* http://testnet.stellarchain.io/
* https://github.com/stellar/java-stellar-sdk
* https://stellar.github.io/java-stellar-sdk/


## Na swipovania medzi príspevkami je potrebné použiť [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
* https://proandroiddev.com/optimizing-nested-recyclerview-a9b7830a4ba7
* https://medium.com/master-of-code-global/recyclerview-tips-and-recipes-476410fa12fd
* https://jonfhancock.com/your-viewholders-are-dumb-make-em-not-dumb-82e6f73f630c
* https://www.reddit.com/r/androiddev/comments/6zex21/optimizing_nested_recyclerview/

## :heavy_check_mark: Na zaznamenávanie chýb počas behu app je povinné použiť [Firebase Crashlytics](https://www.youtube.com/watch?v=k_mdNRZzd30&feature=emb_title) 