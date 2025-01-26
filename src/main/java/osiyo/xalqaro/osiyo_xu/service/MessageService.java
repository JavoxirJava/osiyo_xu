package osiyo.xalqaro.osiyo_xu.service;

import org.springframework.stereotype.Service;
import osiyo.xalqaro.osiyo_xu.entity.Content;
import osiyo.xalqaro.osiyo_xu.entity.Message;
import osiyo.xalqaro.osiyo_xu.repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void addMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getMessageByContent(Content content) {
        return messageRepository.findByContent(content);
    }

    public void deleteMessageByContent(Content content) {
        messageRepository.deleteByContent(content);
    }
}
