/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate;

import java.util.Locale;
import java.util.Map;

import org.hibernate.cfg.Environment;
import org.hibernate.internal.CoreMessageLogger;

import org.jboss.logging.Logger;

/**
 * Describes the methods for multi-tenancy understood by Hibernate.
 *
 * @author Steve Ebersole
 */
public enum MultiTenancyStrategy {
	/**
	 * Multi-tenancy implemented by use of discriminator columns.
	 */
	DISCRIMINATOR,
	/**
	 * Multi-tenancy implemented as separate schemas.
	 */
	SCHEMA,
	/**
	 * Multi-tenancy implemented as separate databases.
	 */
	DATABASE,
	/**
	 * No multi-tenancy.
	 */
	NONE;

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			MultiTenancyStrategy.class.getName()
	);

	/**
	 * Does this strategy indicate a requirement for the specialized
	 * {@link org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider}, rather than the
	 * traditional {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider}?
	 *
	 * @return {@code true} indicates a MultiTenantConnectionProvider is required; {@code false} indicates it is not.
	 */
	public boolean requiresMultiTenantConnectionProvider() {
		return this == DATABASE || this == SCHEMA;
	}

	/**
	 * Extract the MultiTenancyStrategy from the setting map.
	 *
	 * @param properties The map of settings.
	 *
	 * @return The selected strategy.  {@link #NONE} is always the default.
	 */
	public static MultiTenancyStrategy determineMultiTenancyStrategy(Map properties) {
		final Object strategy = properties.get( Environment.MULTI_TENANT );
		if ( strategy == null ) {
			return MultiTenancyStrategy.NONE;
		}

		if ( MultiTenancyStrategy.class.isInstance( strategy ) ) {
			return (MultiTenancyStrategy) strategy;
		}

		final String strategyName = strategy.toString();
		try {
			return MultiTenancyStrategy.valueOf( strategyName.toUpperCase(Locale.ROOT) );
		}
		catch ( RuntimeException e ) {
			LOG.warn( "Unknown multi tenancy strategy [ " +strategyName +" ], using MultiTenancyStrategy.NONE." );
			return MultiTenancyStrategy.NONE;
		}
	}
}
