package ru.maxryazan.bankofryazan.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "settings")
@NoArgsConstructor
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Client authClient;

    @Column(name = "open_personal_with_secret_code")
    private String openPersonalAreaWithSecretCode;

    @Column(name = "do_all_transactions_with_secret_code")
    private String doAllTransactionsWithSecretCode;

    @Column(name = "lock_access_to_personal_area_after_three_try")
    private String lockAccessToPersonalAreaAfterThreeTry;


    public Settings(final Client authClient,
                    final String openPersonalAreaWithSecretCode,
                    final String doAllTransactionsWithSecretCode,
                    String lockAccessToPersonalAreaAfterThreeTry) {
        this.authClient = authClient;
        this.openPersonalAreaWithSecretCode = openPersonalAreaWithSecretCode;
        this.doAllTransactionsWithSecretCode = doAllTransactionsWithSecretCode;
        this.lockAccessToPersonalAreaAfterThreeTry = lockAccessToPersonalAreaAfterThreeTry;
    }
}
