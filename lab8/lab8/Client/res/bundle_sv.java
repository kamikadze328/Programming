import java.util.ListResourceBundle;

public class bundle_sv extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
            {"titleAuth", "Tillstånd"},
            {"login", "Logga in"},
            {"password", "Lösenord"},
            {"signUp", "Bli Medlem"},
            {"logIn", "Logga in"},
            {"logTooShort", "Inloggning måste vara minst 3 tecken lång"},
            {"passTooShort", "Lösenordet måste vara minst 3 tecken lång"},
            {"userAlreadyExists", "Användare finns redan"},
            {"wrongEmail", "Fel e-postadress"},
            {"userDoesntExist", "Användaren finns inte"},
            {"waiting", "Väntar..."},
            {"wrongPassword", "Fel lösenord"},
            {"token", "Tecken:"},
            {"deadToken", "Din tid är över"},
            {"wrongToken", "Fel Token"},
            {"signUpError", "Registrering misslyckades"},
            {"SQLException", "Databas är inte tillgänglig"},
            {"incorrectCommand", " Command är felaktig"},
            {"send", "Skicka"},
            {"title", "Varelse Collection Viewer"},
            {"language", "Språk"},
            {"exit", "utgång"},
            {"info", "Varelse info"},
            {"name", "Namn"},
            {"family", "Familj"},
            {"hunger", "Hunger"},
            {"location", "Plats"},
            {"creationTime", "Creation tid"},
            {"time", "Tid"},
            {"inventory", "Lager"},
            {"user", "Användare"},
            {"format", "formatera"},
            {"size", "Storlek"},
            {"change", "Byta"},
            {"cancel", "Annullera"},
            {"refresh", "Uppdatera"},
            {"disconnected", "Server är tillgänglig"},
            {"connected", "Anslutning: true"},
            {"AddedSuccess", "Inkom Success"},
            {"RemovedSuccess", "Borttagen Success"},
            {"SavedSuccess", "Sparade Success"},
            {"AddedFailing", "Lagt misslyckas"},
            {"RemovedFailingDontYours", "Borttagen misslyckas: Creature inte Yours!"},
            {"SavedFailing", "Sparas inte"},
            {"loadFileError", "Load fil misslyckas"},
            {"JSONError", "Ogiltig JSON format"},
            {"greeting", "Jag undrar vad som kommer att hända ..."},
            {"newCreature", "Ny"},
            {"creatures", "varelser"},
            {"add", "Lägg till"},
            {"add_if_max", "Lägg om Max"},
            {"remove", "Ta bort"},
            {"isEmpty", "fältet är tomt!"},
            {"isnNumber", "Värdet är inte ett nummer!"},
            {"isnPositive", "kan vara positivt!"},
            {"isnChosen", "inte har valts"},
            {"added", "Lagt till"},
            {"tryAgain", "Servern inte är tillgänglig, försök igen senare"},
            {"incorrectDate", "Datumformatet är"},

            {"ChangedSuccess", "Ändrade Success"},
            {"ChangedFailing", "Ändrad misslyckas"},
            {"ChangedFailingDontYours", "Ändrad misslyckas: Creature inte Yours!"},
            {"windowWillBeClosed", "Dead Token. Window kommer att stängas!"},
            {"incorrectX", "Felaktig X!"},
            {"incorrectY", "Felaktig Y!"},
            {"incorrectSize", "Felaktig Storlek"},
            {"incorrectHunger", "Felaktig Hunger!"},
            {"incorrectDate", "Felaktig Date"},
            {"NoCreaturesFound", "Inga varelser found"},
            {"deleted", "Raderade"},
            {"kek", "Du är inte ansluten <br> LOL"},
            {"color", "Färg"},
            {"clear", "Klar"},
            {"CreaturesDoesntChanged", "Varelser förändrades inte"},
            {"ChangedFailedCreatureExists", "Ändrat misslyckades: Skapelsen existerar"},
            {"AddedFailedCreatureExists", "Tillagd misslyckades: Skapelsen existerar"}
    };
}
