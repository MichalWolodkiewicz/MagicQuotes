package motivation.widget.android;


import motivation.widget.android.model.quote.QuotesRepository;
import motivation.widget.android.repository.UserRepository;

public interface RepositoryProvider {

    UserRepository getUserRepository();

    QuotesRepository getQuotesRepository();
}
