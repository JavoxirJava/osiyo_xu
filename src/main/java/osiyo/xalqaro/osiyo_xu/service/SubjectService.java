package osiyo.xalqaro.osiyo_xu.service;

import org.springframework.stereotype.Service;
import osiyo.xalqaro.osiyo_xu.entity.Subject;
import osiyo.xalqaro.osiyo_xu.payload.ApiResponse;
import osiyo.xalqaro.osiyo_xu.repository.SubjectRepository;

import java.util.List;

@Service
public class SubjectService {
    final SubjectRepository subjectRepository;
    final ScienceService scienceService;

    public SubjectService(SubjectRepository subjectRepository, ScienceService scienceService) {
        this.subjectRepository = subjectRepository;
        this.scienceService = scienceService;
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

    public List<String> getSubjectByScience(Long scienceId) {
        return subjectRepository.findByScience(scienceId);
    }

    public boolean isExist(String name) {
        return subjectRepository.findByName(name).isPresent();
    }

    public Subject addSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void editSubjectScience(String subjectName, Long scienceId) {
        Subject subject = subjectRepository.findByName(subjectName).orElse(null);
        assert subject != null;
        subject.setScience(scienceService.getScience(scienceId));
        subjectRepository.save(subject);
    }

    public ApiResponse<?> editSubjectName(String oldName, String name) {
        if (subjectRepository.findByName(name).isPresent())
            return new ApiResponse<>("Bunday fan mavzusi mavjud", false);
        Subject subject = subjectRepository.findByName(oldName).orElse(null);
        assert subject != null;
        subject.setName(name);
        subjectRepository.save(subject);
        return new ApiResponse<>("Fan mavzusi o'zgartirildi", true);
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }
}
