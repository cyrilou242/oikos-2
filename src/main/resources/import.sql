INSERT INTO TodoTask(id, completed, description)
VALUES 
    (nextval('TodoTask_SEQ'), false, 'Aller courrir'),
    (nextval('TodoTask_SEQ'), true, 'Tester quarkus')
    ;

INSERT INTO Recipe(
                   id,
                   cookingTimeCategory,
                   difficulty,
                   isTupperFriendly,
                   isWarm,
                   preparationTimeCategory,
                   requiresWatch,
                   service,
                   totalTimeCategory,
                   description,
                   name,
                   nutritionalTypes,
                   seasonMonths
                   )
VALUES
    (nextval('Recipe_SEQ'),
     'MOYEN', 
     'FACILE', 
     false, 
     true, 
     'RAPIDE', 
     false, 
     'PLAT',
     'MOYEN', 
     'demo description. Do this and do that', 
     'DEMO RECIPE',
     '{"COMPLET"}',
     '{"DECEMBRE"}')
;
