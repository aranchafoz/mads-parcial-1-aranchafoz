import org.junit.*;
import static org.junit.Assert.*;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;

import play.Logger;

import java.sql.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import java.util.List;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import models.Usuario;
import models.Tarea;
import models.UsuarioRepository;
import models.JPAUsuarioRepository;
import models.TareaRepository;
import models.JPATareaRepository;


import services.TareaService;
import services.TareaServiceException;

public class Parcial1 {
   static Database db;
   static private Injector injector;

   // Se ejecuta sólo una vez, al principio de todos los tests
   @BeforeClass
   static public void initApplication() {
      GuiceApplicationBuilder guiceApplicationBuilder =
          new GuiceApplicationBuilder().in(Environment.simple());
      injector = guiceApplicationBuilder.injector();
      db = injector.instanceOf(Database.class);
      // Necesario para inicializar JPA
      injector.instanceOf(JPAApi.class);
   }

   @Before
   public void initData() throws Exception {
      JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTest");
      IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("test/resources/usuarios_dataset.xml"));
      databaseTester.setDataSet(initialDataSet);
      databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
      databaseTester.onSetup();
   }

   private TareaRepository newTareaRepository() {
      return injector.instanceOf(TareaRepository.class);
   }

   private UsuarioRepository newUsuarioRepository() {
      return injector.instanceOf(UsuarioRepository.class);
   }

   private TareaService newTareaService() {
      return injector.instanceOf(TareaService.class);
   }

   @Test
   public void testEjemplo() {
      UsuarioRepository usuarioRepository = newUsuarioRepository();
      TareaRepository tareaRepository = newTareaRepository();
      Usuario usuario = usuarioRepository.findById(1000L);
      Tarea tarea = tareaRepository.findById(1000L);
      assertEquals(2, usuario.getTareas().size());
      assertTrue(usuario.getTareas().contains(tarea));
   }


   @Test(expected = TareaServiceException.class)
   public void testAddTareaNombreDuplicado() {
      TareaService tareaService = newTareaService();
      long idUsuario = 1000L;
      tareaService.nuevaTarea(idUsuario, "Renovar DNI");
   }
}
