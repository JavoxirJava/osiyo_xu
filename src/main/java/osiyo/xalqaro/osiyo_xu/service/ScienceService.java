package osiyo.xalqaro.osiyo_xu.service;

import org.springframework.stereotype.Service;
import osiyo.xalqaro.osiyo_xu.entity.Science;
import osiyo.xalqaro.osiyo_xu.payload.ApiResponse;
import osiyo.xalqaro.osiyo_xu.repository.ScienceRepository;

import java.util.List;

@Service
public class ScienceService {
    final ScienceRepository scienceRepository;

    public ScienceService(ScienceRepository scienceRepository) {
        this.scienceRepository = scienceRepository;
    }

    public List<String> getSciences() {
        return scienceRepository.findAllNames();
    }

    public Science getScience(Long id) {
        return scienceRepository.findById(id).orElse(null);
    }

    public Science getScience(String name) {
        return scienceRepository.findByName(name).orElse(null);
    }

    public ApiResponse<?> addScience(String name) {
        if (scienceRepository.findByName(name).isPresent())
            return new ApiResponse<>("Bunday fan mavzusi mavjud", false);
        scienceRepository.save(Science.builder().name(name).build());
        return new ApiResponse<>("Fan mavzusi qo'shildi", true);
    }

    public ApiResponse<?> editScience(Long id, String name) {
        if (scienceRepository.findByName(name).isPresent())
            return new ApiResponse<>("Bunday fan mavzusi mavjud", false);
        Science science = scienceRepository.findById(id).orElse(null);
        assert science != null;
        science.setName(name);
        scienceRepository.save(science);
        return new ApiResponse<>("Fan mavzusi o'zgartirildi", true);
    }

    public void deleteScienceByName(String name) {
        scienceRepository.deleteById(scienceRepository.findByName(name).get().getId());
    }

    public boolean isExist(String name) {
        return scienceRepository.findByName(name).isPresent();
    }
}
