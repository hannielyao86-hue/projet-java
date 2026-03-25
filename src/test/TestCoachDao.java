// Création
Coach coach1 = new Coach(null, "Pierre Martin", "pierre@coach.com", "Yoga");
coachDao.create(coach1);

// Lecture
Coach coach = coachDao.findById(1);
System.out.println(coach.getNomCoach());

// Modification
        coach.setSpecialite("Pilates");
coachDao.update(coach);

// Liste
List<Coach> coachs = coachDao.findAll();

// Suppression
coachDao.delete(1);