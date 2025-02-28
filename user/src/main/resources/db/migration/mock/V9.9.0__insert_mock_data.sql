INSERT INTO "user" (id,name,password,authority)
VALUES (1, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC',
        'ROLE_ADMIN');

INSERT INTO "user" (id,name,password,authority)
VALUES (2, 'user', '$2a$10$pFhmZCBGtShwhhNPMJZzdeR7.Zcae1JvVcNWsffYuqyZ19OqfL.Pe',
        'ROLE_USER');

INSERT INTO "user" (id,name,password,authority)
VALUES (3, 'second-user', '$2a$10$qj.UcAUjTkFU2r1bEs2jEuh8tFU.uHLE8qKfE1NoAc/szr7Ja/hVa',
        'ROLE_USER');