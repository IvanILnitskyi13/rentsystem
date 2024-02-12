INSERT INTO customer (name)
VALUES
    ('Jan Kowalski'),
    ('Magdalena Badowska'),
    ('Michał Kwiatkowski'),
    ('Barbara Jasińska'),
    ('Andrzej Buczkowski'),

    ('Jolanta Krasowska'),
    ('Antoni Brzeziński'),
    ('Igor Chmielewski'),
    ('Marcin Michalak'),
    ('Gabriel Nowak'),
    ('Nina Cieślak'),
    ('Arleta Ostrowska'),
    ('Alicja Witkowska'),
    ('Daria Michalak'),
    ('Agnieszka Sikorska'),
    ('Weronika Wasilewska');


INSERT INTO rental_object (landlord_id, unit_price, area, description)
VALUES
    (1, 100, 25.2, 'Kawalerka w Poznaniu'),
    (2, 500, 65.2, 'Apartament w Warszawie'),
    (2, 500, 125.73, 'Dom na Mazurach'),
    (3, 120, 14.2, 'Pokój w Karpaczu'),
    (3, 160, 12, 'Pokój w Zakopanem'),
    (4, 5340, 80.4, 'Apartament w Bydgoszczy'),
    (5, 800, 3457.68, 'Hala magazynowa w Warszawie');


INSERT INTO reservation (rental_object_id, tenant_id, start_date, end_date, cost)
VALUES
    (1, 2, '2024-10-10', '2024-10-11', 100),
    (2, 4, '2024-10-10', '2024-10-11', 500),
    (1, 5, '2024-10-11', '2024-10-12', 100);


--  RAPORTY

--  raport, w którym za dany okres czasu znajdziemy informację, przez ile dni był zarezerwowany dany
-- obiekt z informacją ile na nim było rezerwacji.
    SELECT
        SUM(DATEDIFF(day, r.start_date, r.end_date) + 1) AS rent_days,
        COUNT(r.id) AS reservation_count
    FROM reservation r
    WHERE
            r.rental_object_id = 1
      AND r.start_date >= '2024-02-17'
      AND r.end_date <= '2024-10-12';

    -- W przypadku jeśli nas interesują wszystkie rezerwacje w podanym okresie
    SELECT
        r.rental_object_id,
        SUM(DATEDIFF(day, r.start_date, r.end_date) + 1) AS rent_days,
        COUNT(r.id) AS reservation_count
    FROM reservation r
    WHERE
        r.start_date >= '2024-02-17'
        AND r.end_date <= '2024-10-12'
    GROUP BY
        r.rental_object_id;


-- raport, w którym na liście wynajmujących w danym okresie czasu pojawi się informacja – ile
-- obiektów zostało zarezerwowanych, ilu gości przebywało w każdym z obiektów oraz jakie były zyski.

-- UWAGA: ile obiektów zostało zarezerwowanych, ilu gości przebywało w każdym z obiektów -> relacja 1 do wielu
--      (żeby policzyć ilość gości który wynajmowali obiekt, każdy z obiektów powinien być wyświetlony jako oddzielny wiersz)
--      w związku z powyższym wyświetliłem opis obiektu który będzie informować użytkownika o jaki obiekt chodzi

SELECT
    c.NAME AS owner,
    ro.description,
    count(r.tenant_id) AS visitor_number,
    sum(r.cost) AS profits
FROM reservation r
         INNER JOIN rental_object ro
                    ON ro.id = r.rental_object_id
         INNER JOIN customer c
                    ON c.id = ro.landlord_id
WHERE
        r.start_date >= '2024-02-17'
  AND r.end_date <= '2024-10-12'
GROUP BY
    r.rental_object_id
