package osiyo.xalqaro.osiyo_xu.service;

import org.springframework.stereotype.Service;
import osiyo.xalqaro.osiyo_xu.entity.Content;
import osiyo.xalqaro.osiyo_xu.repository.ContentRepository;

import java.util.List;

@Service
public class ContentService {
    final ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<Content> getContentBySubject(String subjectName) {
        return contentRepository.findBySubjectName(subjectName);
    }

    public Content getContent(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    public Content addContent(Content content) {
        return contentRepository.save(content);
    }

    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

    public void deleteContentBySubject(Long subjectId) {
        contentRepository.deleteBySubjectId(subjectId);
    }
}
