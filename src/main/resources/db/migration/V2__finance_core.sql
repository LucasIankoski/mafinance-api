CREATE TABLE category (
                          id          BIGSERIAL PRIMARY KEY,
                          user_id     BIGINT NOT NULL REFERENCES app_user(id),
                          name        VARCHAR(80) NOT NULL,
                          type        VARCHAR(20) NOT NULL,
                          created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX ux_category_user_name_type
    ON category(user_id, name, type);

CREATE TABLE entry_group (
                             id          BIGSERIAL PRIMARY KEY,
                             user_id     BIGINT NOT NULL REFERENCES app_user(id),
                             group_type  VARCHAR(30) NOT NULL,
                             title       VARCHAR(140) NOT NULL,
                             total_amount NUMERIC(19,2),
                             installments_count INT,
                             start_date  DATE,
                             months_generated INT,
                             created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE entry (
                       id           BIGSERIAL PRIMARY KEY,
                       user_id      BIGINT NOT NULL REFERENCES app_user(id),
                       category_id  BIGINT NULL REFERENCES category(id),
                       group_id     BIGINT NULL REFERENCES entry_group(id),

                       entry_type   VARCHAR(20) NOT NULL,
                       description  VARCHAR(180) NOT NULL,
                       amount       NUMERIC(19,2) NOT NULL,
                       entry_date   DATE NOT NULL,
                       paid         BOOLEAN NOT NULL DEFAULT FALSE,

                       installment_no INT NULL,

                       created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX ix_entry_user_date ON entry(user_id, entry_date);
CREATE INDEX ix_entry_group ON entry(group_id);