package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.TrabajadorDTO;

import java.util.*;
import java.util.stream.Collectors;

public class TrabajadorMapper {

    public TrabajadorDTO toDTO(Trabajador trabajador, Long contador){

        TrabajadorDTO trabajadorDTO = new TrabajadorDTO();
                trabajadorDTO.setId(trabajador.getId());
                trabajadorDTO.setDni(trabajador.getDni());
                trabajadorDTO.setNombre(trabajador.getNombre());
                trabajadorDTO.setApellidos(trabajador.getApellido());
                trabajadorDTO.setCargo(trabajador.getCargo());
                trabajadorDTO.setTelefono(trabajador.getTelefono());
                trabajadorDTO.setContador(contador);

        return trabajadorDTO;
    }
}
