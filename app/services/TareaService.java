package services;

import javax.inject.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import models.Usuario;
import models.UsuarioRepository;
import models.Tarea;
import models.TareaRepository;


public class TareaService {
   UsuarioRepository usuarioRepository;
   TareaRepository tareaRepository;

   @Inject
   public TareaService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository) {
      this.usuarioRepository = usuarioRepository;
      this.tareaRepository = tareaRepository;
   }

   // Devuelve la lista de tareas de un usuario, ordenadas por su id
   // (equivalente al orden de creación)
   public List<Tarea> allTareasUsuario(Long idUsuario) {
      Usuario usuario = usuarioRepository.findById(idUsuario);
      if (usuario == null) {
         throw new TareaServiceException("Usuario no existente");
      }
      List<Tarea> tareas = new ArrayList(usuario.getTareas());
      Collections.sort(tareas, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
      return tareas;
   }

   // Devuelve la lista de tareas de un usuario, ordenadas por su nombre
   public List<Tarea> allTareasUsuarioOrdenadasPorNombre(Long idUsuario) {
      Usuario usuario = usuarioRepository.findById(idUsuario);
      if (usuario == null) {
         throw new TareaServiceException("Usuario no existente");
      }
      List<Tarea> tareas = new ArrayList(usuario.getTareas());
      Collections.sort(tareas, (a, b) -> a.getTitulo().compareTo(b.getTitulo()) < 0 ? -1 : a.getTitulo().compareTo(b.getTitulo()) == 0 ? 0 : 1);
      return tareas;
   }

   public Tarea nuevaTarea(Long idUsuario, String titulo) {
      Usuario usuario = usuarioRepository.findById(idUsuario);
      if (usuario == null) {
         throw new TareaServiceException("Usuario no existente");
      }
      List<Tarea> tareas = new ArrayList<Tarea>();
      tareas.addAll(usuario.getTareas());
      for(Tarea t : tareas) {
        if (t.getTitulo().compareToIgnoreCase(titulo) == 0) {
           throw new TareaServiceException("Ya existe una tarea con el mismo título");
        }
      }
      Tarea tarea = new Tarea(usuario, titulo);
      return tareaRepository.add(tarea);
   }

   public Tarea obtenerTarea(Long idTarea) {
      return tareaRepository.findById(idTarea);
   }

   public Tarea modificaTarea(Long idTarea, String nuevoTitulo) {
      Tarea tarea = tareaRepository.findById(idTarea);
      if (tarea == null)
           throw new TareaServiceException("No existe tarea");
      tarea.setTitulo(nuevoTitulo);
      tarea = tareaRepository.update(tarea);
      return tarea;
   }

   public void borraTarea(Long idTarea) {
      Tarea tarea = tareaRepository.findById(idTarea);
      if (tarea == null)
           throw new TareaServiceException("No existe tarea");
      tareaRepository.delete(idTarea);
   }
}
