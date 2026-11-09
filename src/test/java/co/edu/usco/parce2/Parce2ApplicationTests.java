package co.edu.usco.parce2;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class Parce2ApplicationTests {
    @Autowired MockMvc mvc;

    @Test
    void loginPageIsPublic() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "medico", roles = "MEDICO")
    void doctorCannotCreateAppointment() throws Exception {
        mvc.perform(post("/api/consultas").with(csrf())
                .contentType("application/json")
                .content("""
                    {"patientUsername":"paciente","patientName":"Paciente",
                     "reason":"Control","officeNumber":101,
                     "startTime":"2030-01-01T09:00:00","endTime":"2030-01-01T10:00:00",
                     "doctorId":1}
                    """))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "medico", roles = "MEDICO")
    void doctorCanReadOnlyOwnAppointments() throws Exception {
        mvc.perform(get("/api/consultas")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "paciente", roles = "PACIENTE")
    void patientCannotReadDoctors() throws Exception {
        mvc.perform(get("/api/medicos")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "paciente", roles = "PACIENTE")
    void logoutReturnsToLogin() throws Exception {
        mvc.perform(post("/logout").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?logout"));
    }
}
