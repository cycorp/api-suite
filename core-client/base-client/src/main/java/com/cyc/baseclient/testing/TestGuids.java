package com.cyc.baseclient.testing;

/*
 * #%L
 * File: TestGuids.java
 * Project: Base Client
 * %%
 * Copyright (C) 2013 - 2016 Cycorp, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

//import com.cyc.base.TestUtils;
import com.cyc.base.CycAccess;
import com.cyc.base.CycAccessManager;
import com.cyc.base.exception.CycApiException;
import com.cyc.base.exception.CycConnectionException;
import com.cyc.base.annotation.CycObjectLibrary;
import com.cyc.baseclient.CycObjectLibraryLoader;
import com.cyc.session.exception.SessionException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author nwinant
 */
@CycObjectLibrary(requireFieldAnnotations=false)
public class TestGuids {
  
  public static final String AIR_BREATHING_VERTEBRATE_GUID_STRING = "bef7c9c1-9c29-11b1-9dad-c379636f7270";
  public static final String ALGERIA_GUID_STRING = "bd588c92-9c29-11b1-9dad-c379636f7270";
  public static final String ANIMAL_GUID_STRING = "bd58b031-9c29-11b1-9dad-c379636f7270";
  public static final String APPLE_TREE_GUID_STRING = "bd58c19d-9c29-11b1-9dad-c379636f7270";
  public static final String BIOLIGICAL_SPECIES_GUID_STRING = "bd58caeb-9c29-11b1-9dad-c379636f7270";
  public static final String BIOLOGY_VOCABULARY_MT_GUID_STRING = "bdd51776-9c29-11b1-9dad-c379636f7270";
  public static final String BRAZIL_GUID_STRING = "bd588f01-9c29-11b1-9dad-c379636f7270";
  public static final String BURNING_BUSH_GUID_STRING = "be846866-9c29-11b1-9dad-c379636f7270";
  public static final String BUSH_DOG_GUID_STRING = "9cb46f54-ad16-11de-9e34-00219b49082c";
  public static final String CANINE_ANIMAL_GUID_STRING = "bd58d044-9c29-11b1-9dad-c379636f7270";
  public static final String CARNIVORE_ORDER_GUID_STRING = "bd58f431-9c29-11b1-9dad-c379636f7270";
  public static final String CARNIVORE_GUID_STRING = "bd5904f5-9c29-11b1-9dad-c379636f7270";
  public static final String CITY_NAMED_FN_GUID_STRING = "bd6870a6-9c29-11b1-9dad-c379636f7270";
  public static final String CONSTANT_NAME_GUID_STRING = "bd7183b0-9c29-11b1-9dad-c379636f7270";
  public static final String COUNTRY_GUID_STRING = "bd588879-9c29-11b1-9dad-c379636f7270";
  public static final String CYCL_TERM_GUID_STRING = "c107fffb-9c29-11b1-9dad-c379636f7270";
  public static final String DOG_GUID_STRING = TestConstants.DOG.getGuid().getGuidString();
  public static final String DOMESTICATED_ANIMAL_GUID_STRING = "c10c22cd-9c29-11b1-9dad-c379636f7270";
  public static final String DONE_BY_GUID_STRING = "c0fd4798-9c29-11b1-9dad-c379636f7270";
  public static final String EXISTING_OBJECT_TYPE_GUID_STRING = "bd65d880-9c29-11b1-9dad-c379636f7270";
  public static final String FOX_GUID_STRING = "bd58be87-9c29-11b1-9dad-c379636f7270";
  public static final String FRUIT_FN_GUID_STRING = "bd58a976-9c29-11b1-9dad-c379636f7270";
  public static final String GENERAL_LEXICON_MT_GUID_STRING = "c109b867-9c29-11b1-9dad-c379636f7270";
  public static final String INTERNAL_PARTS_GUID_STRING = "bd58cf63-9c29-11b1-9dad-c379636f7270";
  public static final String JACKAL_GUID_STRING = "bd58c2de-9c29-11b1-9dad-c379636f7270";
  public static final String KE_REQUIREMENT_GUID_STRING = "c1141606-9c29-11b1-9dad-c379636f7270";
  public static final String LION_GUID_STRING = "bd58c467-9c29-11b1-9dad-c379636f7270";
  public static final String MALE_HUMAN_GUID_STRING = "bd58d6a1-9c29-11b1-9dad-c379636f7270";
  public static final String MICROTHEORY_GUID_STRING = "bd5880d5-9c29-11b1-9dad-c379636f7270";
  public static final String MODERN_MILITARY_MT_GUID_STRING = "c040a2f0-9c29-11b1-9dad-c379636f7270";
  public static final String MT_SPACE_GUID_STRING = "abb96eb5-e798-11d6-8ac9-0002b3a333c3";
  public static final String MT_TIME_WITH_GRANULARITY_DIM_FN_GUID_STRING = "47537943-331d-11d7-922f-0002b3a333c3";
  public static final String NEAREST_ISA_GUID_STRING = "bf411eed-9c29-11b1-9dad-c379636f7270";
  public static final String NON_PERSON_ANIMAL_GUID_STRING = "bd58e066-9c29-11b1-9dad-c379636f7270";
  public static final String NOW_GUID_STRING = "bd58a068-9c29-11b1-9dad-c379636f7270";
  public static final String OBJECT_TYPE_GUID_STRING = "bd58ab9d-9c29-11b1-9dad-c379636f7270";
  public static final String ORGANISM_CLASSIFICATION_TYPE_GUID_STRING = "bd58dfe4-9c29-11b1-9dad-c379636f7270";
  public static final String ORGANIZATION_GUID_STRING = "bd58d54f-9c29-11b1-9dad-c379636f7270";
  public static final String CAR_ACCIDENT_GUID_STRING = "bd58f4cd-9c29-11b1-9dad-c379636f7270";
  public static final String PARAPHRASE_MT_GUID_STRING = "bf3ab672-9c29-11b1-9dad-c379636f7270";
  public static final String PENGUIN_GUID_STRING = "bd58a986-9c29-11b1-9dad-c379636f7270";
  public static final String PERCENT_OF_REGION_IS_GUID_STRING = "bfb0c6e5-9c29-11b1-9dad-c379636f7270";
  public static final String PERFORMED_BY_GUID_STRING = "bd58a962-9c29-11b1-9dad-c379636f7270";
  public static final String PHYSICAL_DEVICE_GUID_STRING = "bd58c72f-9c29-11b1-9dad-c379636f7270";
  public static final String PITTSBURGH_PENGUINS_GUID_STRING = "c08dec11-9c29-11b1-9dad-c379636f7270";
  public static final String PLANT_GUID_STRING = "bd58c6e1-9c29-11b1-9dad-c379636f7270";
  public static final String PLATO_GUID_STRING = "bd58895f-9c29-11b1-9dad-c379636f7270";
  public static final String RAINDROP_GUID_STRING = "bd58bec6-9c29-11b1-9dad-c379636f7270";
  public static final String RETRIEVER_DOG_GUID_STRING = "bd58e24b-9c29-11b1-9dad-c379636f7270";
  public static final String SIBLINGS_GUID_STRING = "bd58e3e9-9c29-11b1-9dad-c379636f7270";
  public static final String SINGLE_PURPOSE_DEVICE_GUID_STRING = "bd5897aa-9c29-11b1-9dad-c379636f7270";
  public static final String SWAZILAND_GUID_STRING = "bd588a92-9c29-11b1-9dad-c379636f7270";
  public static final String TARGET_GUID_STRING = "c10afaed-9c29-11b1-9dad-c379636f7270";
  public static final String TIMEPOINT_GUID_STRING = "bd58ca05-9c29-11b1-9dad-c379636f7270";
  public static final String TREATY_OAK_GUID_STRING = "bfc0aa80-9c29-11b1-9dad-c379636f7270";
  public static final String UNIVERSE_DATA_MT_GUID_STRING = "bd58d0f3-9c29-11b1-9dad-c379636f7270";
  public static final String VEGETABLE_MATTER_GUID_STRING = "bd58c455-9c29-11b1-9dad-c379636f7270";
  public static final String WOLF_GUID_STRING = "bd58c31f-9c29-11b1-9dad-c379636f7270";
  public static final String WORLD_GEOGRAPHY_MT_GUID_STRING = "bfaac020-9c29-11b1-9dad-c379636f7270";  
  public static final String ONTARIO_CANADIAN_PROVINCE_GUID_STRING = "bd58b6d5-9c29-11b1-9dad-c379636f7270";
  public static final String SUPERTAXONS_GUID_STRING = "bd58e36e-9c29-11b1-9dad-c379636f7270";
  public static final String ENGLISHMT_GUID_STRING = "bd588089-9c29-11b1-9dad-c379636f7270";
  
  public Collection<String> findMissingGuids(CycAccess access)
          throws CycConnectionException, CycApiException, SessionException {
    final Collection<String> results = new ArrayList<String>();
    final CycObjectLibraryLoader objLoader = new CycObjectLibraryLoader(CycAccessManager.getCurrentAccess());
    final Collection<String> objs = objLoader.getAllObjectsForClass(TestGuids.class, String.class);
    for (String str : objs) {
      boolean exists = false;
      try {
        exists = access.getLookupTool().getKnownConstantByGuid(str) != null;
      } catch(CycApiException ex) {
        // For some reason, getKnownConstantByGuid must throw an exception is there's no
        // constant, rather than just sanely returning a null. - nwinant, 2014-07-29
      }
      if (!exists) {
        results.add(str);
      }
      //System.out.println("  - " + exists + "  " + str);
    }
    return results;
  }
}
