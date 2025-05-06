package com.app.inventario.Service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.app.inventario.Dto.ProveedorDto;
import com.app.inventario.Dto.ServiceResult;
import com.app.inventario.Models.Proveedor;
import com.app.inventario.Repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;
    private static final Logger logger = LoggerFactory.getLogger(Proveedor.class);
    //    EXPRESIONES PARA VALIDAR DATOS CON REGEX
    private static final Pattern RUT_PATTERN = Pattern.compile("^\\d{1,8}-[\\dkK]$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?56?\\d{9}$");

    public ServiceResult<Proveedor> addProveedor(ProveedorDto request) {
        List<String> errors = new ArrayList<>();
        try {
            if (request.getNombre() == null || request.getNombre().isBlank()) {
                errors.add("El nombre es obligatorio");
            }

            if (request.getRut() == null || request.getRut().isBlank()) {
                errors.add("El RUT es obligatorio");
            } else if (!validarRutChileno(request.getRut())) {
                errors.add("El RUT no es válido");
            }

            if (request.getEmail() != null && !request.getEmail().isBlank()
                    && !EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
                errors.add("El formato del email es inválido");
            }

            if (request.getTelefono() != null && !request.getTelefono().isBlank()
                    && !PHONE_PATTERN.matcher(request.getTelefono()).matches()) {
                errors.add("El formato del teléfono es inválido");
            }

            if (!errors.isEmpty()) {
                return new ServiceResult<>(errors);
            }

            if (proveedorRepository.existsByRut(normalizarRut(request.getRut()))) {
                errors.add("El RUT ya está registrado");
                return new ServiceResult<>(errors);
            }

            Proveedor proveedor = Proveedor.builder()
                    .nombre(request.getNombre())
                    .rut(normalizarRut(request.getRut()))
                    .direccion(request.getDireccion())
                    .email(request.getEmail())
                    .telefono(request.getTelefono())
                    .activo(true)
                    .build();

            Proveedor proveedorSaved = proveedorRepository.save(proveedor);
            logger.info("Proveedor guardado exitosamente: {}", proveedorSaved.getId());
            return new ServiceResult<>(proveedorSaved);

        } catch (Exception e) {
            logger.error("Error creando proveedor: ", e);
            errors.add("Error interno al crear proveedor: " + e.getMessage());
            return new ServiceResult<>(errors);
        }
    }

    private boolean validarRutChileno(String rut) {
        String rutLimpio = normalizarRut(rut);

        if (!RUT_PATTERN.matcher(rutLimpio).matches()) {
            return false;
        }

        String[] partes = rutLimpio.split("-");
        String cuerpo = partes[0];
        String dv = partes[1].toUpperCase();

        int suma = 0;
        int multiplicador = 2;

        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(cuerpo.charAt(i)) * multiplicador;
            multiplicador = multiplicador == 7 ? 2 : multiplicador + 1;
        }

        int resto = suma % 11;
        String dvCalculado = switch (11 - resto) {
            case 11 -> "0";
            case 10 -> "K";
            default -> String.valueOf(11 - resto);
        };

        return dv.equals(dvCalculado);
    }

    private String normalizarRut(String rut) {
        return rut.replaceAll("[^0-9kK-]", "").toUpperCase();
    }}
