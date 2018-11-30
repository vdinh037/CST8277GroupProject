/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.id;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ObjectNameNormalizer;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.SessionImpl;
import org.hibernate.type.StandardBasicTypes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.hibernate.testing.boot.MetadataBuildingContextTestingImpl;
import org.hibernate.testing.junit4.BaseUnitTestCase;

import static org.junit.Assert.assertEquals;

/**
 * I went back to 3.3 source and grabbed the code/logic as it existed back then and crafted this
 * unit test so that we can make sure the value keep being generated in the expected manner
 *
 * @author Steve Ebersole
 */
@SuppressWarnings({"deprecation"})
@RequiresDialectFeature(DialectChecks.SupportsSequences.class)
public class SequenceHiLoGeneratorNoIncrementTest extends BaseUnitTestCase {
	private static final String TEST_SEQUENCE = "test_sequence";

	private StandardServiceRegistry serviceRegistry;
	private SessionFactoryImplementor sessionFactory;
	private SequenceStyleGenerator generator;
	private SessionImplementor sessionImpl;
	private SequenceValueExtractor sequenceValueExtractor;

	@Before
	public void setUp() throws Exception {
		serviceRegistry = new StandardServiceRegistryBuilder()
				.enableAutoClose()
				.applySetting( AvailableSettings.HBM2DDL_AUTO, "create-drop" )
				.build();

		generator = new SequenceStyleGenerator();

		// Build the properties used to configure the id generator
		Properties properties = new Properties();
		properties.setProperty( SequenceStyleGenerator.SEQUENCE_PARAM, TEST_SEQUENCE );
		properties.setProperty( SequenceStyleGenerator.OPT_PARAM, "legacy-hilo" );
		properties.setProperty( SequenceStyleGenerator.INCREMENT_PARAM, "0" ); // JPA allocationSize of 1
		properties.put(
				PersistentIdentifierGenerator.IDENTIFIER_NORMALIZER,
				new ObjectNameNormalizer() {
					@Override
					protected MetadataBuildingContext getBuildingContext() {
						return new MetadataBuildingContextTestingImpl( serviceRegistry );
					}
				}
		);
		generator.configure( StandardBasicTypes.LONG, properties, serviceRegistry );

		final Metadata metadata = new MetadataSources( serviceRegistry ).buildMetadata();
		generator.registerExportables( metadata.getDatabase() );

		sessionFactory = (SessionFactoryImplementor) metadata.buildSessionFactory();
		sequenceValueExtractor = new SequenceValueExtractor( sessionFactory.getDialect(), TEST_SEQUENCE );
	}

	@After
	public void tearDown() throws Exception {
		if ( sessionImpl != null && !sessionImpl.isClosed() ) {
			((Session) sessionImpl).close();
		}
		if ( sessionFactory != null ) {
			sessionFactory.close();
		}
		if ( serviceRegistry != null ) {
			StandardServiceRegistryBuilder.destroy( serviceRegistry );
		}
	}

	@Test
	public void testHiLoAlgorithm() {
		sessionImpl = (SessionImpl) sessionFactory.openSession();

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// historically the hilo generators skipped the initial block of values;
		// 		so the first generated id value is maxlo + 1, here be 4
		assertEquals( 1L, generateValue() );

		// which should also perform the first read on the sequence which should set it to its "start with" value (1)
		assertEquals( 1L, extractSequenceValue() );

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		assertEquals( 2L, generateValue() );
		assertEquals( 2L, extractSequenceValue() );

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		assertEquals( 3L, generateValue() );
		assertEquals( 3L, extractSequenceValue() );

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		assertEquals( 4L, generateValue() );
		assertEquals( 4L, extractSequenceValue() );

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		assertEquals( 5L, generateValue() );
		assertEquals( 5L, extractSequenceValue() );

		((Session) sessionImpl).close();
	}

	private long extractSequenceValue() {
		return sequenceValueExtractor.extractSequenceValue( sessionImpl );
	}

	private long generateValue() {
		Long generatedValue;
		Transaction transaction = ((Session) sessionImpl).beginTransaction();
		generatedValue = (Long) generator.generate( sessionImpl, null );
		transaction.commit();
		return generatedValue.longValue();
	}
}
