package it.live.brainbox.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Primary
public class BrainBoxBot extends TelegramLongPollingBot {

    private final String botToken;

    public BrainBoxBot(@Value("${telegram.botToken}") String botToken, DefaultBotOptions botOptions) {
        this.botToken = botToken;
    }

    public void sendToChannel(String name, String movieName) throws TelegramApiException {
        execute(new SendMessage("@whatthefuckiston", "\n name = " + name + "\n movieName = " + movieName + "\n type = MovieSuggest"));
    }

    @Override
    public String getBotUsername() {
        return "@brainboxxbot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            String firstName = update.getMessage().getChat().getFirstName();
            try {
                if (text.equals("/start")) {
                    execute(new SendMessage(String.valueOf(chatId), "Assalomu alaykum hurmatli " + firstName + ". Takliflaringizni yuboring"));
                } else {
                    execute(new SendMessage(String.valueOf(chatId), "Taklifingiz uchun rahmat hurmatli " + firstName + ". Biz tez orada taklifingizni ko'rib chiqamiz😊"));
                    execute(new SendMessage("@whatthefuckiston", "chatId = " + chatId + "\n username = @"+update.getMessage().getChat().getUserName() + "\n name = " + firstName + "\n message = " + text + "\n type = PersonalSuggest"));
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

}
