package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import Controller.ActiviterController;
import Controller.InscriptionController;
import Controller.MembreController;
import dao.*;
import exception.BusinessException;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controller JavaFX pour la vue principale.
 * Gère l'interface utilisateur et fait le lien avec les controllers métier.
 */
public class MainViewController {

    // CONTROLLERS MÉTIER

    private final MembreController membreController = new MembreController();
    private final ActiviterController activiteController = new ActiviterController();
    private final InscriptionController inscriptionController = new InscriptionController();
    private final AbonnementDao abonnementDao = new AbonnementDaoImpl();
    private final CoachDao coachDao = new CoachDaoImpl();
    private final SalleDao salleDao = new SalleDaoImpl();

    // COMPOSANTS FXML - MEMBRES

    @FXML private TableView<Membre> membreTableView;
    @FXML private TableColumn<Membre, Integer> membreIdColumn;
    @FXML private TableColumn<Membre, String> membreNomColumn;
    @FXML private TableColumn<Membre, String> membreEmailColumn;
    @FXML private TableColumn<Membre, String> membrePhoneColumn;
    @FXML private TableColumn<Membre, String> membreAbonnementColumn;
    @FXML private TextField searchMembreField;

    // COMPOSANTS FXML - ACTIVITÉS

    @FXML private TableView<Activiter> activiteTableView;
    @FXML private TableColumn<Activiter, Integer> activiteIdColumn;
    @FXML private TableColumn<Activiter, String> activiteNomColumn;
    @FXML private TableColumn<Activiter, String> activiteDateColumn;
    @FXML private TableColumn<Activiter, String> activiteHeureDebutColumn;
    @FXML private TableColumn<Activiter, String> activiteHeureFinColumn;
    @FXML private TableColumn<Activiter, String> activiteCoachColumn;
    @FXML private TableColumn<Activiter, String> activiteSalleColumn;

    // COMPOSANTS FXML - INSCRIPTIONS

    @FXML private TableView<Inscription> inscriptionTableView;
    @FXML private TableColumn<Inscription, Integer> inscriptionIdColumn;
    @FXML private TableColumn<Inscription, String> inscriptionMembreColumn;
    @FXML private TableColumn<Inscription, String> inscriptionActiviteColumn;
    @FXML private TableColumn<Inscription, String> inscriptionDateColumn;
    @FXML private TableColumn<Inscription, String> inscriptionStatutColumn;

    // AUTRES COMPOSANTS

    @FXML private Label statusLabel;

    // MÉTHODE D'INITIALISATION

    /**
     * Méthode appelée automatiquement après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        System.out.println(" Initialisation de l'interface JavaFX...");

        // Configuration des tableaux
        setupMembreTable();
        setupActiviteTable();
        setupInscriptionTable();

        // Chargement initial des données
        handleRafraichirMembres();
        handleRafraichirActivites();
        handleRafraichirInscriptions();

        updateStatus("Application prête");
        System.out.println(" Interface JavaFX initialisée avec succès");
    }

    // CONFIGURATION DES TABLEAUX

    /**
     * Configure le tableau des membres.
     */
    private void setupMembreTable() {
        membreIdColumn.setCellValueFactory(new PropertyValueFactory<>("idMembre"));
        membreNomColumn.setCellValueFactory(new PropertyValueFactory<>("nomMembre"));
        membreEmailColumn.setCellValueFactory(new PropertyValueFactory<>("emailMembre"));
        membrePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Colonne personnalisée pour l'abonnement
        membreAbonnementColumn.setCellValueFactory(cellData -> {
            Membre membre = cellData.getValue();
            if (membre.getIdAbonnement() != null) {
                Abonnement abonnement = abonnementDao.findById(membre.getIdAbonnement());
                return new javafx.beans.property.SimpleStringProperty(
                        abonnement != null ? abonnement.getNomAbonnement() : "Aucun"
                );
            }
            return new javafx.beans.property.SimpleStringProperty("Aucun");
        });
    }

    /**
     * Configure le tableau des activités.
     */
    private void setupActiviteTable() {
        activiteIdColumn.setCellValueFactory(new PropertyValueFactory<>("idActivite"));
        activiteNomColumn.setCellValueFactory(new PropertyValueFactory<>("nomActivite"));

        // Formatage de la date
        activiteDateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDateActivite();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new javafx.beans.property.SimpleStringProperty(date.format(formatter));
        });

        // Formatage des heures
        activiteHeureDebutColumn.setCellValueFactory(cellData -> {
            LocalTime heure = cellData.getValue().getHeureDebut();
            return new javafx.beans.property.SimpleStringProperty(heure.toString());
        });

        activiteHeureFinColumn.setCellValueFactory(cellData -> {
            LocalTime heure = cellData.getValue().getHeureFin();
            return new javafx.beans.property.SimpleStringProperty(heure.toString());
        });

        // Coach
        activiteCoachColumn.setCellValueFactory(cellData -> {
            Coach coach = cellData.getValue().getCoach();
            return new javafx.beans.property.SimpleStringProperty(
                    coach != null ? coach.getNomCoach() : "N/A"
            );
        });

        // Salle
        activiteSalleColumn.setCellValueFactory(cellData -> {
            Salle salle = cellData.getValue().getSalle();
            return new javafx.beans.property.SimpleStringProperty(
                    salle != null ? salle.getNomSalle() : "Non assignée"
            );
        });
    }

    /**
     * Configure le tableau des inscriptions.
     */
    private void setupInscriptionTable() {
        inscriptionIdColumn.setCellValueFactory(new PropertyValueFactory<>("idInscription"));

        // Membre
        inscriptionMembreColumn.setCellValueFactory(cellData -> {
            Membre membre = cellData.getValue().getMembre();
            return new javafx.beans.property.SimpleStringProperty(
                    membre != null ? membre.getNomMembre() : "N/A"
            );
        });

        // Activité
        inscriptionActiviteColumn.setCellValueFactory(cellData -> {
            Activiter activite = cellData.getValue().getActivite();
            return new javafx.beans.property.SimpleStringProperty(
                    activite != null ? activite.getNomActivite() : "N/A"
            );
        });

        // Date
        inscriptionDateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDateInscription();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new javafx.beans.property.SimpleStringProperty(date.format(formatter));
        });

        // Statut
        inscriptionStatutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    // HANDLERS - MEMBRES

    /**
     * Rafraîchit la liste des membres.
     */
    @FXML
    private void handleRafraichirMembres() {
        try {
            List<Membre> membres = membreController.getTousMembres();
            ObservableList<Membre> observableList = FXCollections.observableArrayList(membres);
            membreTableView.setItems(observableList);
            updateStatus(membres.size() + " membre(s) chargé(s)");
        } catch (Exception e) {
            showError("Erreur lors du chargement des membres", e.getMessage());
        }
    }

    /**
     * Ajoute un nouveau membre.
     */
    @FXML
    private void handleAjouterMembre() {
        try {
            // Dialogue de saisie
            Dialog<Membre> dialog = new Dialog<>();
            dialog.setTitle("Nouveau membre");
            dialog.setHeaderText("Saisir les informations du membre");

            // Boutons
            ButtonType btnValider = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnValider, ButtonType.CANCEL);

            // Formulaire
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nomField = new TextField();
            nomField.setPromptText("Nom complet");
            TextField emailField = new TextField();
            emailField.setPromptText("email@exemple.com");
            TextField phoneField = new TextField();
            phoneField.setPromptText("0612345678");

            ComboBox<Abonnement> abonnementCombo = new ComboBox<>();
            abonnementCombo.setItems(FXCollections.observableArrayList(abonnementDao.findAll()));
            abonnementCombo.setPromptText("Sélectionner un abonnement");

            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nomField, 1, 0);
            grid.add(new Label("Email :"), 0, 1);
            grid.add(emailField, 1, 1);
            grid.add(new Label("Téléphone :"), 0, 2);
            grid.add(phoneField, 1, 2);
            grid.add(new Label("Abonnement :"), 0, 3);
            grid.add(abonnementCombo, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // Conversion du résultat
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnValider) {
                    Integer idAbonnement = abonnementCombo.getValue() != null
                            ? abonnementCombo.getValue().getIdAbonnement()
                            : null;

                    return membreController.creerMembre(
                            nomField.getText(),
                            emailField.getText(),
                            phoneField.getText(),
                            idAbonnement
                    );
                }
                return null;
            });

            Optional<Membre> result = dialog.showAndWait();
            result.ifPresent(membre -> {
                handleRafraichirMembres();
                showInfo("Membre ajouté", "Le membre " + membre.getNomMembre() + " a été ajouté avec succès.");
            });

        } catch (BusinessException e) {
            showError("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ajouter le membre : " + e.getMessage());
        }
    }

    /**
     * Modifie le membre sélectionné.
     */
    @FXML
    private void handleModifierMembre() {
        Membre selected = membreTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner un membre à modifier.");
            return;
        }

        try {
            // Dialogue de modification (similaire à l'ajout)
            Dialog<Membre> dialog = new Dialog<>();
            dialog.setTitle("Modifier membre");
            dialog.setHeaderText("Modifier les informations de " + selected.getNomMembre());

            ButtonType btnValider = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnValider, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nomField = new TextField(selected.getNomMembre());
            TextField emailField = new TextField(selected.getEmailMembre());
            TextField phoneField = new TextField(selected.getPhone());

            ComboBox<Abonnement> abonnementCombo = new ComboBox<>();
            List<Abonnement> abonnements = abonnementDao.findAll();
            abonnementCombo.setItems(FXCollections.observableArrayList(abonnements));

            if (selected.getIdAbonnement() != null) {
                Abonnement currentAbo = abonnementDao.findById(selected.getIdAbonnement());
                abonnementCombo.setValue(currentAbo);
            }

            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nomField, 1, 0);
            grid.add(new Label("Email :"), 0, 1);
            grid.add(emailField, 1, 1);
            grid.add(new Label("Téléphone :"), 0, 2);
            grid.add(phoneField, 1, 2);
            grid.add(new Label("Abonnement :"), 0, 3);
            grid.add(abonnementCombo, 1, 3);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnValider) {
                    Integer idAbonnement = abonnementCombo.getValue() != null
                            ? abonnementCombo.getValue().getIdAbonnement()
                            : null;

                    membreController.modifierMembre(
                            selected.getIdMembre(),
                            nomField.getText(),
                            emailField.getText(),
                            phoneField.getText(),
                            idAbonnement
                    );
                    return selected;
                }
                return null;
            });

            Optional<Membre> result = dialog.showAndWait();
            result.ifPresent(membre -> {
                handleRafraichirMembres();
                showInfo("Membre modifié", "Le membre a été modifié avec succès.");
            });

        } catch (BusinessException e) {
            showError("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            showError("Erreur", "Impossible de modifier le membre : " + e.getMessage());
        }
    }

    /**
     * Supprime le membre sélectionné.
     */
    @FXML
    private void handleSupprimerMembre() {
        Membre selected = membreTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner un membre à supprimer.");
            return;
        }

        // Confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer le membre " + selected.getNomMembre() + " ?");
        confirmation.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                membreController.supprimerMembre(selected.getIdMembre());
                handleRafraichirMembres();
                showInfo("Membre supprimé", "Le membre a été supprimé avec succès.");
            } catch (Exception e) {
                showError("Erreur", "Impossible de supprimer le membre : " + e.getMessage());
            }
        }
    }

    /**
     * Recherche des membres.
     */
    @FXML
    private void handleRechercherMembre() {
        String recherche = searchMembreField.getText();

        if (recherche == null || recherche.trim().isEmpty()) {
            handleRafraichirMembres();
            return;
        }

        try {
            List<Membre> membres = membreController.rechercherParNom(recherche);
            ObservableList<Membre> observableList = FXCollections.observableArrayList(membres);
            membreTableView.setItems(observableList);
            updateStatus(membres.size() + " membre(s) trouvé(s)");
        } catch (Exception e) {
            showError("Erreur de recherche", e.getMessage());
        }
    }

    // HANDLERS - ACTIVITÉS

    @FXML
    private void handleRafraichirActivites() {
        try {
            List<Activiter> activites = activiteController.getToutesActivites();
            ObservableList<Activiter> observableList = FXCollections.observableArrayList(activites);
            activiteTableView.setItems(observableList);
            updateStatus(activites.size() + " activité(s) chargée(s)");
        } catch (Exception e) {
            showError("Erreur lors du chargement des activités", e.getMessage());
        }
    }

    @FXML
    private void handleAjouterActivite() {
        try {
            Dialog<Activiter> dialog = new Dialog<>();
            dialog.setTitle("Nouvelle activité");
            dialog.setHeaderText("Saisir les informations de l'activité");

            ButtonType btnValider = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnValider, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nomField = new TextField();
            nomField.setPromptText("Nom de l'activité");

            DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(1));

            TextField heureDebutField = new TextField("09:00");
            heureDebutField.setPromptText("HH:MM");

            TextField heureFinField = new TextField("10:00");
            heureFinField.setPromptText("HH:MM");

            ComboBox<Coach> coachCombo = new ComboBox<>();
            coachCombo.setItems(FXCollections.observableArrayList(coachDao.findAll()));
            coachCombo.setPromptText("Sélectionner un coach");

            ComboBox<Salle> salleCombo = new ComboBox<>();
            salleCombo.setItems(FXCollections.observableArrayList(salleDao.findAll()));
            salleCombo.setPromptText("Sélectionner une salle");

            grid.add(new Label("Nom :"), 0, 0);
            grid.add(nomField, 1, 0);
            grid.add(new Label("Date :"), 0, 1);
            grid.add(datePicker, 1, 1);
            grid.add(new Label("Heure début :"), 0, 2);
            grid.add(heureDebutField, 1, 2);
            grid.add(new Label("Heure fin :"), 0, 3);
            grid.add(heureFinField, 1, 3);
            grid.add(new Label("Coach :"), 0, 4);
            grid.add(coachCombo, 1, 4);
            grid.add(new Label("Salle :"), 0, 5);
            grid.add(salleCombo, 1, 5);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnValider) {
                    Integer idSalle = salleCombo.getValue() != null
                            ? salleCombo.getValue().getIdSalle()
                            : null;

                    return activiteController.creerActivite(
                            nomField.getText(),
                            datePicker.getValue(),
                            LocalTime.parse(heureDebutField.getText()),
                            LocalTime.parse(heureFinField.getText()),
                            coachCombo.getValue().getIdCoach(),
                            idSalle
                    );
                }
                return null;
            });

            Optional<Activiter> result = dialog.showAndWait();
            result.ifPresent(activite -> {
                handleRafraichirActivites();
                showInfo("Activité ajoutée", "L'activité a été ajoutée avec succès.");
            });

        } catch (BusinessException e) {
            showError("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ajouter l'activité : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifierActivite() {
        showInfo("Fonctionnalité", "Modification d'activité à implémenter");
    }

    @FXML
    private void handleSupprimerActivite() {
        Activiter selected = activiteTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner une activité à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer l'activité " + selected.getNomActivite() + " ?");
        confirmation.setContentText("Cette action supprimera aussi toutes les inscriptions associées.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                activiteController.supprimerActivite(selected.getIdActivite());
                handleRafraichirActivites();
                showInfo("Activité supprimée", "L'activité a été supprimée avec succès.");
            } catch (Exception e) {
                showError("Erreur", "Impossible de supprimer l'activité : " + e.getMessage());
            }
        }
    }

    // HANDLERS - INSCRIPTIONS

    @FXML
    private void handleRafraichirInscriptions() {
        try {
            List<Inscription> inscriptions = inscriptionController.getToutesInscriptions();
            ObservableList<Inscription> observableList = FXCollections.observableArrayList(inscriptions);
            inscriptionTableView.setItems(observableList);
            updateStatus(inscriptions.size() + " inscription(s) chargée(s)");
        } catch (Exception e) {
            showError("Erreur lors du chargement des inscriptions", e.getMessage());
        }
    }

    @FXML
    private void handleAjouterInscription() {
        try {
            Dialog<Inscription> dialog = new Dialog<>();
            dialog.setTitle("Nouvelle inscription");
            dialog.setHeaderText("Inscrire un membre à une activité");

            ButtonType btnValider = new ButtonType("Inscrire", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnValider, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            ComboBox<Membre> membreCombo = new ComboBox<>();
            membreCombo.setItems(FXCollections.observableArrayList(membreController.getTousMembres()));
            membreCombo.setPromptText("Sélectionner un membre");

            ComboBox<Activiter> activiteCombo = new ComboBox<>();
            activiteCombo.setItems(FXCollections.observableArrayList(activiteController.getToutesActivites()));
            activiteCombo.setPromptText("Sélectionner une activité");

            grid.add(new Label("Membre :"), 0, 0);
            grid.add(membreCombo, 1, 0);
            grid.add(new Label("Activité :"), 0, 1);
            grid.add(activiteCombo, 1, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnValider) {
                    return inscriptionController.inscrireMembre(
                            membreCombo.getValue().getIdMembre(),
                            activiteCombo.getValue().getIdActivite(),
                            "en_attente"
                    );
                }
                return null;
            });

            Optional<Inscription> result = dialog.showAndWait();
            result.ifPresent(inscription -> {
                handleRafraichirInscriptions();
                showInfo("Inscription ajoutée", "L'inscription a été créée avec succès.");
            });

        } catch (BusinessException e) {
            showError("Erreur de validation", e.getMessage());
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ajouter l'inscription : " + e.getMessage());
        }
    }

    @FXML
    private void handleConfirmerInscription() {
        Inscription selected = inscriptionTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner une inscription.");
            return;
        }

        try {
            inscriptionController.confirmerInscription(selected.getIdInscription());
            handleRafraichirInscriptions();
            showInfo("Inscription confirmée", "L'inscription a été confirmée.");
        } catch (Exception e) {
            showError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleAnnulerInscription() {
        Inscription selected = inscriptionTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner une inscription.");
            return;
        }

        try {
            inscriptionController.annulerInscription(selected.getIdInscription());
            handleRafraichirInscriptions();
            showInfo("Inscription annulée", "L'inscription a été annulée.");
        } catch (Exception e) {
            showError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleSupprimerInscription() {
        Inscription selected = inscriptionTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner une inscription.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer cette inscription ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                inscriptionController.supprimerInscription(selected.getIdInscription());
                handleRafraichirInscriptions();
                showInfo("Inscription supprimée", "L'inscription a été supprimée.");
            } catch (Exception e) {
                showError("Erreur", e.getMessage());
            }
        }
    }

    // MÉTHODES UTILITAIRES

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}