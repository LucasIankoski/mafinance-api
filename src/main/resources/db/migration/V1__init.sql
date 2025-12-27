CREATE TABLE app_user (
                          id              BIGSERIAL PRIMARY KEY,
                          email           VARCHAR(320) NOT NULL,
                          name            VARCHAR(120) NOT NULL,
                          password_hash   VARCHAR(255) NOT NULL,
                          role            VARCHAR(40)  NOT NULL DEFAULT 'USER',
                          enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
                          created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                          updated_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX ux_app_user_email ON app_user (email);
