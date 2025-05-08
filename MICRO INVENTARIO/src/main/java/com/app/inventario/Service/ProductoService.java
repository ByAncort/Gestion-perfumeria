package com.app.inventario.Service;

import com.app.inventario.Dto.*;
import com.app.inventario.Models.*;
import com.app.inventario.Repository.CategoriaRepository;
import com.app.inventario.Repository.ProductoRepository;
import com.app.inventario.Repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    public ServiceResult<Producto> crearProducto(ProductoDto dto) {
        List<String> errors = new ArrayList<>();
        try {
            // Validaciones
            if(dto.getCodigoSku() == null || dto.getCodigoSku().isBlank()) {
                errors.add("El SKU es obligatorio");
            }

            if(productoRepository.existsByCodigoSku(dto.getCodigoSku())) {
                errors.add("El SKU ya existe");
            }

            if(dto.getSerial() != null && productoRepository.existsBySerial(dto.getSerial())) {
                errors.add("El serial ya está registrado");
            }

            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            if(!errors.isEmpty()) {
                return new ServiceResult<>(errors);
            }

            Producto producto = Producto.builder()
                    .codigoSku(dto.getCodigoSku())
                    .nombre(dto.getNombre())
                    .descripcion(dto.getDescripcion())
                    .precio(dto.getPrecio())
                    .costo(dto.getCosto())
                    .catalogo(dto.getCatalogo())
                    .serial(dto.getSerial())
                    .categoria(categoria)
                    .build();

            return new ServiceResult<>(productoRepository.save(producto));

        } catch(Exception e) {
            errors.add("Error: " + e.getMessage());
            return new ServiceResult<>(errors);
        }
    }

    public ServiceResult<Producto> buscarPorSerial(String serial) {
        List<String> errors = new ArrayList<>();

        try {
            // Validación 1: Formato del serial
            if (serial == null || serial.isBlank()) {
                errors.add("El serial no puede estar vacío");
                return new ServiceResult<>(errors);
            }

            // Validación 2: Patrón específico (ejemplo: 3 letras + 6 números)
            if (!Pattern.matches("^[A-Za-z]{3}\\d{6}$", serial)) {
                errors.add("Formato de serial inválido");
                return new ServiceResult<>(errors);
            }

            // Validación 3: Existencia en BD
            Producto producto = productoRepository.findBySerial(serial)
                    .orElseThrow(() -> new RuntimeException("Serial no registrado"));

            // Validación 4: Producto activo
            if (!producto.getActivo()) {
                errors.add("Producto no disponible");
                return new ServiceResult<>(errors);
            }

            return new ServiceResult<>(producto);

        } catch (RuntimeException e) {
            errors.add(e.getMessage());
            return new ServiceResult<>(errors);
        } catch (Exception e) {
            logger.error("Error crítico al buscar serial {}", serial, e);
            errors.add("Error en el sistema");
            return new ServiceResult<>(errors);
        }
    }
    // Métodos adicionales (actualizar, buscar por serial, etc.)
}