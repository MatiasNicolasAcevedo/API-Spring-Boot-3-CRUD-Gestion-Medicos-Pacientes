package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired // no se recomienda para unit test. sino que con getter. / se puede usar con qualifier
    private MedicoRepository medicoRepository;

    @PostMapping
    public void registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico) { // sin anotacion no leera cuerpo de la solicitud ni va a mapear sus campos al DTO recibido como parámetro. @Valid para validar.
        medicoRepository.save(new Medico(datosRegistroMedico));
    }

    @GetMapping
    public Page<DatosListadoMedico> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion) { // Page para paginar lo que retorna. Pageable recibe del front. @PageableDefault(size = 2) para config paginacion por defecto.
        // return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);  mapea un objeto dto: DatosListadoMedico(Medico medico).
        return medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new); // crea metodo con la nomenclatura se spring. en el repository.
    }

    @PutMapping
    @Transactional // abrir transaccion, con jpa para mapear, cuando termine el metodo, la transaccion se va a liberar, se hace el commit.
    public void actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico) {
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
    }

    // DELETE LOGICO
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
    }

    // DELETE EN BASE DE DATOS
    // public void eliminarMedico(@PathVariable Long id) {
    //     Medico medico = medicoRepository.getReferenceById(id);
    //     medicoRepository.delete(medico);
    // }

}
