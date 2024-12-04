package osiyo.xalqaro.osiyo_xu.service;

import org.springframework.stereotype.Service;
import osiyo.xalqaro.osiyo_xu.entity.Subject;
import osiyo.xalqaro.osiyo_xu.repository.SubjectRepository;

import java.util.List;

@Service
public class SubjectService {
    final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubject(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    public Subject getSubject(String name) {
        return subjectRepository.findByName(name).orElse(null);
    }

    public boolean isExist(String name) {
        return subjectRepository.findByName(name).isPresent();
    }

    public Subject addSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
